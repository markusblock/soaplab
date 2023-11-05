package org.soaplab.ui.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.beanutils.PropertyUtils;
import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.grid.CellFocusEvent.GridSection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
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

public abstract class EntityTableView<T extends NamedEntity> extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private H1 title;

	@Getter
	private final EntityRepository<T> repository;

	Grid<T> grid;
	BeanValidationBinder<T> binder;
	HeaderRow searchHeaderRow;
	EntityFilter<T> entityFilter;
	boolean entityChanged = false;
	boolean editorCanceled = false;
	Optional<T> focusedEntity = Optional.empty();

	Map<String, SerializablePredicate<T>> searchFilter = new HashMap<>();

	public EntityTableView(Class<T> entityClass, EntityRepository<T> repository) {
		super();
		this.repository = repository;

		setSizeFull();

		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		add(title);

		grid = new Grid<>(entityClass, false);
		grid.setColumnReorderingAllowed(true);
		add(grid);

		grid.getHeaderRows().clear();
		searchHeaderRow = grid.appendHeaderRow();
		entityFilter = new EntityFilter<>();
		binder = new BeanValidationBinder<T>(entityClass);

		Editor<T> editor = grid.getEditor();
		editor.setBinder(binder);
		editor.setBuffered(false);

		editor.addCancelListener(l -> {
			System.out.println("Cancel");
		});

		editor.addCloseListener(l -> {
			System.out.println("close");
			if (entityChanged && !editorCanceled) {
				System.out.println("save");
				T entity = l.getItem();
				repository.update(entity);
			}
			resetEditorState();
		});

		editor.addOpenListener(l -> {
			System.out.println("open");
			resetEditorState();

			l.getSource().getBinder().addValueChangeListener(e -> {
				Object oldValue = e.getOldValue();
				Object newValue = e.getValue();
				if (!ObjectUtils.nullSafeEquals(oldValue, newValue)) {
					entityChanged = true;
				}
			});
		});

		grid.addItemDoubleClickListener(e -> {
			grid.getEditor().editItem(e.getItem());
			Component editorComponent = e.getColumn().getEditorComponent();
			if (editorComponent instanceof Focusable<?> focusableComponent) {
				focusableComponent.focus();
			}
		});
		addEnterHandler(grid);
		addEscapeHandler(grid);
		grid.addCellFocusListener(event -> {
			System.out.println("cell focused (section: %s)".formatted(event.getSection()));
			if (event.getSection() == GridSection.BODY) {
				focusedEntity = event.getItem();
			} else {
				focusedEntity = Optional.empty();
			}
		});

		refreshTable();

		// addTextColumn(Entity.Fields.id, "domain.entity.id");
		addNameColumn(NamedEntity.Fields.name, "domain.entity.name");
	}

	private void resetEditorState() {
		editorCanceled = false;
		entityChanged = false;
		focusedEntity = Optional.empty();
	}

	private void addEscapeHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			System.out.println("Escape pressed");
			if (grid.getEditor().isOpen()) {
				editorCanceled = true;
				grid.getEditor().cancel();
			}
			// Note! some browsers return key as Escape and some as Esc
		}).setFilter("event.key === 'Escape' || event.key === 'Esc'");
	}

	private void addEnterHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			System.out.println("Enter pressed");

			if (grid.getEditor().isOpen()) {
				grid.getEditor().closeEditor();
			} else {
				focusedEntity.ifPresentOrElse(selectedEntity -> grid.getEditor().editItem(selectedEntity),
						() -> grid.getSelectionModel().getFirstSelectedItem()
								.ifPresent(selectedEntity -> grid.getEditor().editItem(selectedEntity)));
			}
		}).setFilter("event.key === 'Enter'");
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
		refreshTable();
	}

	private void refreshTable() {
		ListDataProvider<T> dataProvider = new ListDataProvider(repository.findAll());
		GridListDataView<T> gridListDataView = grid.setItems(dataProvider);
		entityFilter.setDataView(gridListDataView);
	}

	protected void addNameColumn(String propertyName, String id) {
		addColumn(propertyName, id).setResizable(true).setAutoWidth(true).setFlexGrow(1);
	}

	protected void addIntegerColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new StringToIntegerConverter(""));
	}

	protected void addBigDecimalColumn(String propertyName, String id) {
		addPropertyColumn(propertyName, id, new MyStringToBigDecConverter(""));
	}

	protected <PROPERTY_TYPE> Column<T> addPropertyColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		return addColumn(propertyName, id, converter).setAutoWidth(false).setFlexGrow(0).setWidth("7em");
	}

	protected <PROPERTY_TYPE> Column<T> addColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		final TextField entityField = createTextField(id);
		addEscapeHandler(entityField);
		addEnterHandler(entityField);
		binder.forField(entityField).withNullRepresentation("").withConverter(converter).bind(propertyName);
		Grid.Column<T> column = grid.addColumn(propertyName).setHeader(getTranslation(id))
				.setEditorComponent(entityField);
		searchHeaderRow.getCell(column)
				.setComponent(createFilterHeader(propertyName, id, entityFilter::setFilterToProperty));
		return column;
	}

	protected Column<T> addColumn(String propertyName, String id) {
		final TextField entityField = createTextField(id);
		addEscapeHandler(entityField);
		addEnterHandler(entityField);
		binder.forField(entityField).withNullRepresentation("").bind(propertyName);
		Grid.Column<T> column = grid.addColumn(propertyName).setHeader(getTranslation(id))
				.setEditorComponent(entityField);
		searchHeaderRow.getCell(column)
				.setComponent(createFilterHeader(propertyName, id, entityFilter::setFilterToProperty));
		return column;
	}

	private Component createFilterHeader(String propertyName, String id,
			BiConsumer<String, String> filterChangeConsumer) {
		String headerValue = getTranslation(id);
		NativeLabel label = new NativeLabel(headerValue);
		label.getStyle().set("padding-top", "var(--lumo-space-m)").set("font-size", "var(--lumo-font-size-xs)");
		TextField textField = new TextField();
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.setClearButtonVisible(true);
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		textField.setWidthFull();
		textField.getStyle().set("max-width", "100%");
		textField.addValueChangeListener(e -> filterChangeConsumer.accept(propertyName, e.getValue()));
		VerticalLayout layout = new VerticalLayout(label, textField);
		layout.setWidthFull();
		layout.getThemeList().clear();
		layout.getThemeList().add("spacing-xs");

		return layout;
	}

	private static class EntityFilter<T> {
		private GridListDataView<T> dataView;

		private Map<String, String> filterPropertyNameSearchTermPairs = new HashMap<>();

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
			Set<Map.Entry<String, String>> entries = filterPropertyNameSearchTermPairs.entrySet();
			for (Map.Entry<String, String> entry : entries) {
				String entityPropertyValue = extractPropertyValueFromEntity(entity, entry.getKey());
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
