package org.soaplab.ui.views;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.soaplab.domain.NamedEntity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.data.value.ValueChangeMode;

import lombok.AccessLevel;
import lombok.Getter;

public class EntityList<T extends NamedEntity> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;

	@Getter(value = AccessLevel.PROTECTED)
	private Grid<T> entityGrid;

	@Getter(value = AccessLevel.PROTECTED)
	private EntityTableListener<T> callback;

	private TextField searchField;
	private final Button addButton;
	private final Button removeButton;

	public EntityList(EntityTableListener<T> callback) {
		super();
		this.callback = callback;

		content = new VerticalLayout();
		content.setSizeFull();
		add(content);

		HorizontalLayout toolPanel = new HorizontalLayout();
		toolPanel.setWidthFull();
		content.add(toolPanel);

		searchField = new TextField();
		searchField.setId("entitylist.search");
		searchField.setWidthFull();
		searchField.setPlaceholder("Search");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.addValueChangeListener(event -> filterEntityList(event.getValue()));
		toolPanel.add(searchField);

		addButton = new Button();
		addButton.setId("entitylist.add");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(event -> {
//			callback.createNewEntity();
		});
		toolPanel.add(addButton);

		removeButton = new Button();
		removeButton.setId("entitylist.remove");
		removeButton.setIcon(VaadinIcon.MINUS.create());
		removeButton.addClickListener(event -> {
//			callback.deleteEntity(CollectionUtils.firstElement(entityGrid.getSelectedItems()));
		});
		toolPanel.add(removeButton);

		entityGrid = new Grid<T>();
		entityGrid.setId("entitylist.grid");
		entityGrid.setSelectionMode(SelectionMode.SINGLE);
		entityGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		entityGrid.setSelectionMode(SelectionMode.SINGLE);
		entityGrid.addColumn(T::getName).setHeader(getTranslation("domain.entity.name")).setSortable(true);
		content.add(entityGrid);

		addSelectionListener();

		setItems();
	}

	private void filterEntityList(String searchString) {
//		if (StringUtils.isEmpty(searchString)) {
//			entityGrid.setItems(callback.getRepository().findAll());
//		} else {
//			entityGrid.setItems(getFilteredEntities(searchString));
//		}
	}

	protected List<T> getFilteredEntities(String searchString) {
//		return callback.getRepository().findByName(searchString);
		return null;
	}

	private void setItems() {
		filterEntityList(null);
	}

	public void select(T selectEntity) {
		if (selectEntity == null) {
			return;
		}
		entityGrid.select(selectEntity);
	}

	public void deselectAll() {
		entityGrid.deselectAll();
	}

	public void refreshAll() {
		setItems();
	}

	public void refresh(T entity) {
		setItems();
	}

	void addSelectionListener() {
		SingleSelect<Grid<T>, T> entitySelect = entityGrid.asSingleSelect();
		entitySelect.addValueChangeListener(e -> {
//			callback.entitySelected(e.getValue());
			removeButton.setEnabled(e.getValue() != null);
		});
	}

	public void selectFirstEntity() {
		List<T> entities = entityGrid.getListDataView().getItems().collect(Collectors.toList());
		if (entities.size() > 0) {
			select(entities.get(0));
		} else {
			select(null);
		}
	}

	public Optional<T> getSelectedEntity() {
		SingleSelect<Grid<T>, T> entitySelect = entityGrid.asSingleSelect();
		return entitySelect.getOptionalValue();
	}

	public void listenToSelectionChanges() {
		entityGrid.setEnabled(true);
		searchField.setEnabled(true);
		addButton.setEnabled(true);
		removeButton.setEnabled(true);
	}

	public void ignoreSelectionChanges() {
		entityGrid.setEnabled(false);
		searchField.setEnabled(false);
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
}
