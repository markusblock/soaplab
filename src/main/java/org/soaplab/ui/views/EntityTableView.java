package org.soaplab.ui.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.beanutils.PropertyUtils;
import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.CellFocusEvent.GridSection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
public abstract class EntityTableView<T extends NamedEntity> extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private final H1 title;

	@Getter
	private final EntityRepository<T> repository;

	private final Button addButton;
	private final Button removeButton;
	private final Grid<T> grid;
	private final BeanValidationBinder<T> binder;
	private final HeaderRow searchHeaderRow;
	private final EntityFilter<T> entityFilter;

	private boolean entityChanged = false;
	private boolean editorCanceled = false;
	private Optional<T> focusedEntity = Optional.empty();
	private Optional<Column<T>> focusedColumn = Optional.empty();

	Map<String, SerializablePredicate<T>> searchFilter = new HashMap<>();

	public EntityTableView(Class<T> entityClass, EntityRepository<T> repository, boolean createEntityFunction) {
		super();
		this.repository = repository;

		setSizeFull();

		grid = new Grid<>(entityClass, false);
		// <theme-editor-local-classname>
		grid.addClassName("entity-table-view-grid-1");
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		final HorizontalLayout headerPanel = new HorizontalLayout();
		headerPanel.setWidthFull();
		add(headerPanel);

		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		headerPanel.add(title);

		addButton = new Button();
		addButton.setId("entitylist.add");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(event -> {
			log.trace("add button clicked");
			if (grid.getEditor().isOpen()) {
				grid.getEditor().closeEditor();
			}
			final T newEntity = createNewEmptyEntity();
			focusedColumn = Optional.of(grid.getColumns().get(0));
			grid.getListDataView().addItem(newEntity);
			editEntity(newEntity);
		});
		addButton.setEnabled(createEntityFunction);
		headerPanel.add(addButton);

		removeButton = new Button();
		removeButton.setId("entitylist.remove");
		removeButton.setIcon(VaadinIcon.MINUS.create());
		removeButton.addClickListener(event -> {
			grid.getSelectionModel().getFirstSelectedItem().ifPresent(entity -> deleteEntity(entity));
		});
		removeButton.setEnabled(false);
		headerPanel.add(removeButton);

		grid.setColumnReorderingAllowed(true);
		add(grid);

		grid.getHeaderRows().clear();
		searchHeaderRow = grid.appendHeaderRow();
		entityFilter = new EntityFilter<>();
		binder = new BeanValidationBinder<T>(entityClass);

		final Editor<T> editor = grid.getEditor();
		editor.setBinder(binder);
		editor.setBuffered(false);

		addEditorCancelListener(editor);
		addEditorCloseListener(repository, editor);
		addEditorOpenListener(editor);
		addGridClickListener();
		addGridDoubleClickListener();
		addEnterHandler(grid);
		addEscapeHandler(grid);
		addGridCellFocusListener();
		grid.setSelectionMode(SelectionMode.SINGLE);
		addGridSelectionListener();

		// addTextColumn(Entity.Fields.id, "domain.entity.id");
		addNameColumn(NamedEntity.Fields.name, "domain.entity.name");
	}

	public EntityTableView(Class<T> entityClass, EntityRepository<T> repository) {
		this(entityClass, repository, true);
	}

	private void addGridSelectionListener() {
		grid.addSelectionListener(event -> {
			log.trace("selected %s".formatted(event.getFirstSelectedItem()));
			event.getFirstSelectedItem().ifPresentOrElse(selection -> {
				focusedEntity = Optional.of(selection);
				removeButton.setEnabled(true);
			}, () -> removeButton.setEnabled(false));
		});
	}

	private void addGridClickListener() {
		grid.addItemClickListener(event -> {
			log.trace("clicked %s".formatted(event.getItem()));
			focusedEntity = Optional.ofNullable(event.getItem());
			focusedColumn = Optional.ofNullable(event.getColumn());
		});
	}

	private void addEditorCancelListener(final Editor<T> editor) {
		editor.addCancelListener(l -> {
			log.trace("Cancel");
		});
	}

	private void addEditorCloseListener(EntityRepository<T> repository, final Editor<T> editor) {
		editor.addCloseListener(l -> {
			log.trace("close editor");
			if (entityChanged && !editorCanceled) {
				final T entity = l.getItem();
				if (entity.getId() == null) {
					log.trace("create");
					repository.create(entity);
				} else {
					log.trace("update");
					repository.update(entity);
				}
				grid.getDataProvider().refreshItem(entity);
			}
			resetEditorState();
			refreshTable();
		});
	}

	private void addEditorOpenListener(final Editor<T> editor) {
		editor.addOpenListener(l -> {
			log.trace("open editor");
			resetEditorState();
			removeButton.setEnabled(false);

			focusedColumn.ifPresentOrElse(column -> focusComponent(column.getEditorComponent()),
					() -> focusComponent(grid.getColumns().get(0)));

			l.getSource().getBinder().addValueChangeListener(e -> {
				final Object oldValue = e.getOldValue();
				final Object newValue = e.getValue();
				if (!ObjectUtils.nullSafeEquals(oldValue, newValue)) {
					log.trace("value changed from %s to %s".formatted(Objects.toString(oldValue),
							Objects.toString(newValue)));
					entityChanged = true;
				}
			});
		});
	}

	private void addGridDoubleClickListener() {
		grid.addItemDoubleClickListener(e -> {
			log.trace("double clicked");
			grid.getEditor().editItem(e.getItem());
			focusComponent(e.getColumn().getEditorComponent());
		});
	}

	private void addGridCellFocusListener() {
		grid.addCellFocusListener(event -> {
			log.trace("cell focused (section: %s)".formatted(event.getSection()));
			if (event.getSection() == GridSection.BODY) {
				if (grid.getEditor().isOpen()) {
					log.trace("column %s".formatted(event.getColumn().get().getHeaderText()));
					focusedColumn = event.getColumn();
				} else {
					log.trace("cell focused (old: %s)".formatted(focusedEntity));
					log.trace("cell focused (new: %s)".formatted(event.getItem()));
					event.getItem().ifPresent(item -> {
						grid.getSelectionModel().deselectAll();
						focusedEntity = event.getItem();
						focusedColumn = event.getColumn();
					});
				}
			} else {
				log.trace("reset focus");
				focusedEntity = Optional.empty();
				focusedColumn = Optional.empty();
				if (grid.getEditor().isOpen()) {
					grid.getEditor().closeEditor();
				}
			}
		});
	}

	private void focusComponent(final Component component) {
		if (component instanceof final Focusable<?> focusableComponent) {
			focusableComponent.focus();
		}
	}

	protected abstract T createNewEmptyEntity();

	private void deleteEntity(T entity) {
		log.trace("delete button pressed");
		repository.delete(entity.getId());
		refreshTable();
	}

	private void resetEditorState() {
		editorCanceled = false;
		entityChanged = false;
	}

	private void addEscapeHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			log.trace("Escape pressed");
			if (grid.getEditor().isOpen()) {
				editorCanceled = true;
				grid.getEditor().cancel();
			}
			// Note! some browsers return key as Escape and some as Esc
		}).setFilter("event.key === 'Escape' || event.key === 'Esc'");
	}

	private void addEnterHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			log.trace("Enter pressed");

			if (grid.getEditor().isOpen()) {
				grid.getEditor().closeEditor();
			} else {
				editEntity();
			}
		}).setFilter("event.key === 'Enter'");
	}

	private void editEntity(T entity) {
		log.trace("Edit entity %s".formatted(entity));
		grid.getEditor().editItem(entity);
	}

	private void editEntity() {
		focusedEntity.ifPresentOrElse(focusedEntity -> editEntity(focusedEntity), () -> grid.getSelectionModel()
				.getFirstSelectedItem().ifPresent(selectedEntity -> editEntity(selectedEntity)));
	}

	private TextField createTextField(String id) {
		final TextField propertyField = new TextField();
		propertyField.setId(id);
		propertyField.setWidthFull();
		return propertyField;
	}

	protected abstract String getHeader();

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		log.trace("beforeEnter");
		refreshTable();
	}

	private void refreshTable() {
		log.trace("refreshTable");
		final ListDataProvider<T> dataProvider = new ListDataProvider<T>(repository.findAll());
		final GridListDataView<T> gridListDataView = grid.setItems(dataProvider);
		entityFilter.setDataView(gridListDataView);
		grid.recalculateColumnWidths();
	}

	protected void addNameColumn(String propertyName, String id) {
		addColumn(propertyName, id).setAutoWidth(true).setFlexGrow(1);
	}

	protected void addIntegerColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new StringToIntegerConverter(""));
	}

	protected void addBigDecimalColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new MyStringToBigDecConverter(""));
	}

	protected void addPercentageColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new StringToPercentageConverter());
		// TODO set suffix component
//		final TextField propertyField = createPropertyTextField(id);
//		propertyField.setSuffixComponent(new Div(new Text("%")));
//		editablePropertyFields.add(propertyField);
//		propertySection.addFormItem(propertyField, getTranslation(id));
//		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToPercentageConverter())
//				.bind(getter, setter);
	}

	protected void addPriceColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new StringToPriceValueConverter());
		// TODO set suffix component
//		grid.addColumn(LitRenderer.<T>of("<b>${item.cost} €</b>").withProperty("cost",
//				ingredient -> new StringToPriceValueConverter().convertToPresentation(ingredient.getCost(),
//						new ValueContext())));
	}

	protected <PROPERTY_TYPE> Column<T> addPropertyColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		return addColumn(propertyName, id, converter);
	}

	protected <PROPERTY_TYPE> Column<T> addColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		final TextField entityField = createTextField(id);
		binder.forField(entityField).withNullRepresentation("").withConverter(converter).bind(propertyName);
		final Grid.Column<T> column = grid.addColumn(propertyName).setEditorComponent(entityField);
		column.setResizable(true);
		searchHeaderRow.getCell(column)
				.setComponent(createFilterHeader(propertyName, id, entityFilter::setFilterToProperty));
		return column;
	}

	protected Column<T> addColumn(String propertyName, String id) {
		final TextField propertyEditor = createTextField(id);
		propertyEditor.setWidthFull();
		binder.forField(propertyEditor).withNullRepresentation("").bind(propertyName);
		final Grid.Column<T> column = grid.addColumn(propertyName).setEditorComponent(propertyEditor);
		column.setResizable(true);
		searchHeaderRow.getCell(column)
				.setComponent(createFilterHeader(propertyName, id, entityFilter::setFilterToProperty));
		return column;
	}

	private Component createFilterHeader(String propertyName, String id,
			BiConsumer<String, String> filterChangeConsumer) {
		final String headerValue = getTranslation(id);
		final NativeLabel label = new NativeLabel(headerValue);
		label.getStyle().set("padding-top", "var(--lumo-space-m)").set("font-size", "var(--lumo-font-size-xs)");

		final TextField textField = new TextField();
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.setClearButtonVisible(true);
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		textField.addValueChangeListener(e -> filterChangeConsumer.accept(propertyName, e.getValue()));

		final VerticalLayout layout = new VerticalLayout(label, textField);
		layout.getThemeList().clear();
		layout.getThemeList().add("spacing-xs");

		return layout;
	}

	private static class EntityFilter<T> {
		private GridListDataView<T> dataView;

		private final Map<String, String> filterPropertyNameSearchTermPairs = new HashMap<>();

		public EntityFilter() {
		}

		public void setDataView(GridListDataView<T> dataView) {
			this.dataView = dataView;
			this.dataView.addFilter(this::test);
		}

		public void setFilterToProperty(String propertyName, String searchTerm) {
			this.filterPropertyNameSearchTermPairs.put(propertyName, searchTerm);
			this.dataView.refreshAll();
		}

		public boolean test(T entity) {
			final Set<Map.Entry<String, String>> entries = filterPropertyNameSearchTermPairs.entrySet();
			for (final Map.Entry<String, String> entry : entries) {
				final String entityPropertyValue = extractPropertyValueFromEntity(entity, entry.getKey());
				if (!matches(entityPropertyValue, entry.getValue())) {
					return false;
				}
			}
			return true;
		}

		@SneakyThrows
		public String extractPropertyValueFromEntity(T entity, String propertyName) {
			return ObjectUtils.nullSafeToString(PropertyUtils.getProperty(entity, propertyName));
		}

		private boolean matches(String entityPropertyValue, String searchTerm) {
			return ObjectUtils.isEmpty(searchTerm)
					|| entityPropertyValue.toLowerCase().contains(searchTerm.toLowerCase());
		}
	}
}
