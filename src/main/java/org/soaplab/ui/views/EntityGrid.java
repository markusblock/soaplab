package org.soaplab.ui.views;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.soaplab.domain.Entity;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.CellFocusEvent.GridSection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityGrid<T extends Entity> extends Grid<T> {

	private static final long serialVersionUID = 1L;

	private EntityTableListener<T> tableListener;

	@Getter
	private final Binder<T> binder;
	@Getter
	private final EntityFilter<T> entityFilter;

	private boolean entityChanged = false;
	private boolean editorCanceled = false;
	private Optional<T> focusedEntity = Optional.empty();
	private Optional<Column<T>> focusedColumn = Optional.empty();

	public EntityGrid(Class<T> entityClass, EntityTableListener<T> tableListener) {
		super(entityClass, false);

		this.tableListener = tableListener;
		entityFilter = new EntityFilter<>();

		addClassName("entity-table-view-grid-1");
		setId("entitygrid.%s".formatted(entityClass.getSimpleName()));
		addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		setColumnReorderingAllowed(true);

		binder = new BeanValidationBinder<T>(entityClass);

		final Editor<T> editor = getEditor();
		editor.setBinder(binder);
		editor.setBuffered(true);

		addEditorOpenListener(editor);
		addEditorCloseListener(editor);

		addGridClickListener();
		addGridDoubleClickListener();
		addEnterHandler(this);
		addEscapeHandler(this);

		addGridCellFocusListener();

		setSelectionMode(SelectionMode.SINGLE);
		addGridSelectionListener();
	}

	public EntityGrid() {
		super();

		entityFilter = new EntityFilter<>();

		addClassName("entity-table-view-grid-1");
		addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		setColumnReorderingAllowed(true);

		getHeaderRows().clear();
		binder = new Binder<>();

		final Editor<T> editor = getEditor();
		editor.setBinder(binder);
		editor.setBuffered(true);

		addEditorOpenListener(editor);
		addEditorCloseListener(editor);
		addEditorSaveListener(editor);

		addGridClickListener();
		addGridDoubleClickListener();
		addEnterHandler(this);
		addEscapeHandler(this);

		addGridCellFocusListener();

		setSelectionMode(SelectionMode.SINGLE);
		addGridSelectionListener();
	}

	private void addEditorOpenListener(final Editor<T> editor) {
		editor.addOpenListener(l -> {
			log.trace("%s: open editor".formatted(getId()));
			resetEditorState();

			tableListener.entityTableEntersEditMode();

			focusedColumn.ifPresentOrElse(column -> focusComponent(column.getEditorComponent()),
					() -> focusComponent(getColumns().get(0)));

			l.getSource().getBinder().addValueChangeListener(e -> {
				final Object oldValue = e.getOldValue();
				final Object newValue = e.getValue();
				if (!ObjectUtils.nullSafeEquals(oldValue, newValue)) {
					log.trace("%s: value changed from %s to %s".formatted(getId(), Objects.toString(oldValue),
							Objects.toString(newValue)));
					entityChanged = true;
				}
			});
		});
	}

	private void addEditorSaveListener(final Editor<T> editor) {
		editor.addSaveListener(l -> {
			log.trace("%s: save editor".formatted(getId()));
//			if (entityChanged && !editorCanceled) {
//				final T entity = l.getItem();
//				binder.writeBeanIfValid(entity);
//				tableListener.entityChangedInEntityTable(entity);
//				getDataProvider().refreshItem(entity);
//			}
//			resetEditorState();
//			tableListener.entityTableLeavesEditMode();
		});
	}

	private void addEditorCloseListener(final Editor<T> editor) {
		editor.addCloseListener(l -> {
			log.trace("%s: close editor".formatted(getId()));
			if (entityChanged && !editorCanceled) {
				final T entity = l.getItem();
				binder.writeBeanIfValid(entity);
				tableListener.entityChangedInEntityTable(entity);
				getDataProvider().refreshItem(entity);
			}
			resetEditorState();
			tableListener.entityTableLeavesEditMode();
		});
	}

	private void addGridClickListener() {
		addItemClickListener(event -> {
			log.trace("%s: clicked %s".formatted(getId(), event.getItem()));
			focusedEntity = Optional.ofNullable(event.getItem());
			focusedColumn = Optional.ofNullable(event.getColumn());
		});
	}

	private void addGridDoubleClickListener() {
		addItemDoubleClickListener(e -> {
			log.trace("%s: double clicked".formatted(getId()));
			getEditor().editItem(e.getItem());
			focusComponent(e.getColumn().getEditorComponent());
		});
	}

	private void addEnterHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			log.trace("%s: Enter pressed".formatted(getId()));

			if (getEditor().isOpen()) {
				editorCanceled = false;
				getEditor().save();
			} else {
				editEntity();
			}
		}).setFilter("event.key === 'Enter'");
	}

	private void addEscapeHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			log.trace("%s: Escape pressed".formatted(getId()));
			if (getEditor().isOpen()) {
				editorCanceled = true;
				getEditor().cancel();
			}
			// Note! some browsers return key as Escape and some as Esc
		}).setFilter("event.key === 'Escape' || event.key === 'Esc'");
	}

	private void addGridCellFocusListener() {
		addCellFocusListener(event -> {
			log.trace("%s: cell focused (section: %s)".formatted(getId(), event.getSection()));
			if (event.getSection() == GridSection.BODY) {
				if (getEditor().isOpen()) {
					log.trace("%s: column %s".formatted(getId(), event.getColumn().get().getId()));
					focusedColumn = event.getColumn();
				} else {
					log.trace("%s: cell focused (old: %s)".formatted(getId(), focusedEntity));
					log.trace("%s: cell focused (new: %s)".formatted(getId(), event.getItem()));
					event.getItem().ifPresent(item -> {
						getSelectionModel().deselectAll();
						focusedEntity = event.getItem();
						focusedColumn = event.getColumn();
					});
				}
			} else {
				log.trace("%s: reset focus".formatted(getId()));
				focusedEntity = Optional.empty();
				focusedColumn = Optional.empty();
				if (getEditor().isOpen()) {
					getEditor().cancel();
				}
			}
		});
	}

	private void addGridSelectionListener() {
		addSelectionListener(event -> {
			log.trace("%s: selected %s".formatted(getId(), event.getFirstSelectedItem()));
//			event.getFirstSelectedItem().ifPresentOrElse(selection -> {
//				focusedEntity = Optional.of(selection);
//				removeButton.setEnabled(true);
//			}, () -> removeButton.setEnabled(false));
			event.getFirstSelectedItem().ifPresentOrElse(selection -> {
				focusedEntity = Optional.of(selection);
				tableListener.selectionChangedInEntityTable(Optional.of(selection));
			}, () -> select(focusedEntity.orElse(null)));
		});
	}

	private void resetEditorState() {
		editorCanceled = false;
		entityChanged = false;
	}

	private void focusComponent(final Component component) {
		if (component instanceof final Focusable<?> focusableComponent) {
			focusableComponent.focus();
		}
	}

	private void editEntity(T entity) {
		log.trace("%s: Edit entity %s".formatted(getId(), entity));
		getEditor().editItem(entity);
	}

	private void editEntity() {
		focusedEntity.ifPresentOrElse(focusedEntity -> editEntity(focusedEntity), () -> getSelectionModel()
				.getFirstSelectedItem().ifPresent(selectedEntity -> editEntity(selectedEntity)));
	}

	protected TextField createTextField(String id) {
		final TextField propertyField = new TextField();
		propertyField.setId(id + ".editor");
		propertyField.setWidthFull();
		return propertyField;
	}

	protected Column<T> addEntityNameColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id).setAutoWidth(true).setFlexGrow(1);
	}

	protected Column<T> addIntegerColumn(String propertyName, String id) {
		return addEntityPropertyColumn(propertyName, id, new StringToIntegerConverter(""));
	}

	protected Column<T> addBigDecimalColumn(String propertyName, String id) {
		return addEntityPropertyColumn(propertyName, id, new MyStringToBigDecConverter());
	}

	protected Column<T> addPercentageColumn(String propertyName, String id) {
		return addEntityPropertyColumn(propertyName, id, new StringToPercentageConverter(), new Div(new Text("%")));
	}

	protected Column<T> addPriceColumn(String propertyName, String id) {
		return addEntityPropertyColumn(propertyName, id, new StringToPriceValueConverter(), new Div(new Text("€")));
	}

	protected <PROPERTY_TYPE> Column<T> addEntityPropertyColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		return addEntityColumn(propertyName, id, converter, null);
	}

	protected <PROPERTY_TYPE> Column<T> addEntityPropertyColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter, Component suffixComponent) {
		return addEntityColumn(propertyName, id, converter, suffixComponent);
	}

	protected <PROPERTY_TYPE> Column<T> addEntityColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter, Component suffixComponent) {
		final String fullyQualifiedId = getId().orElseThrow() + "." + id;
		final TextField entityField = createTextField(fullyQualifiedId);
		if (suffixComponent != null) {
			entityField.setSuffixComponent(suffixComponent);
		}
		if (converter == null) {
			getBinder().forField(entityField).withNullRepresentation("").bind(propertyName);
		} else {
			getBinder().forField(entityField).withNullRepresentation("").withConverter(converter).bind(propertyName);
		}
		final Grid.Column<T> column = addColumn(propertyName).setEditorComponent(entityField);
		column.setId(fullyQualifiedId);
		column.setResizable(true);
		return column;
	}

	protected Column<T> addEntityColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, null, null);
	}

	public void selectFirstEntity() {
		final List<T> entities = getListDataView().getItems().collect(Collectors.toList());
		if (entities.size() > 0) {
			select(entities.get(0));
		} else {
			select(null);
		}
	}

	public void selectEntity(T selectEntity) {
		if (selectEntity == null) {
			return;
		}
		select(selectEntity);
	}

	public void setEntities(List<T> entities) {
		log.trace("%s: setEntities".formatted(getId()));
		final ListDataProvider<T> dataProvider = new ListDataProvider<T>(entities);
		final GridListDataView<T> gridListDataView = setItems(dataProvider);
		entityFilter.setDataView(gridListDataView);
		recalculateColumnWidths();
	}

	public void refreshEntity(T entity) {
		log.trace("%s: refreshEntity %s".formatted(getId(), entity));
		getListDataView().refreshItem(entity);
		recalculateColumnWidths();
		select(entity);
	}

	public void cancelEditMode() {
		log.trace("%s: cancelEditMode".formatted(getId()));
		if (getEditor().isOpen()) {
			getEditor().cancel();
		}
	}

	public void clearSelection() {
		log.trace("%s: clearSelection".formatted(getId()));
		deselectAll();

	}

	public Optional<T> getSelectedEntity() {
		return getSelectionModel().getFirstSelectedItem();
	}

	public List<T> getEntities() {
		return getListDataView().getItems().toList();
	}

	public void setEntityTableListener(EntityTableListener<T> entityTableListener) {
		this.tableListener = entityTableListener;
	}

	public void removeEntity(T entity) {
		log.trace("%s: removeEntity %s".formatted(getId(), entity));
		getListDataView().removeItem(entity);
		selectFirstEntity();
	}

	public void openEditorOnEntity(T entity) {
		focusedEntity = Optional.of(entity);
		editEntity();
	}

}
