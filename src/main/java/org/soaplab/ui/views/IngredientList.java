package org.soaplab.ui.views;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.soaplab.domain.Ingredient;

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

public class IngredientList<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;

	private Grid<T> ingredientGrid;

	private IngredientsViewListControllerCallback<T> callback;

	private TextField searchField;

	public IngredientList(IngredientsViewListControllerCallback<T> callback) {
		super();
		this.callback = callback;

		content = new VerticalLayout();
		content.setSizeFull();
		add(content);

		HorizontalLayout toolPanel = new HorizontalLayout();
		toolPanel.setWidthFull();
		content.add(toolPanel);

		searchField = new TextField();
		searchField.setId("ingredientlist.search");
		searchField.setWidthFull();
		searchField.setPlaceholder("Search");
		searchField.setClearButtonVisible(true);
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.addValueChangeListener(event -> filterIngredientByNameOrInci(event.getValue()));
		toolPanel.add(searchField);

		ingredientGrid = new Grid<T>();
		ingredientGrid.setId("ingredientlist.grid");
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addColumn(T::getName).setHeader(getTranslation("domain.ingredient.name")).setSortable(true);
		ingredientGrid.addColumn(T::getInci).setHeader(getTranslation("domain.ingredient.inci")).setSortable(true);
		content.add(ingredientGrid);

		addSelectionListener();

		setItems();
	}

	private void filterIngredientByNameOrInci(String searchString) {
		if (StringUtils.isEmpty(searchString)) {
			ingredientGrid.setItems(callback.getRepository().findAll());
		} else {
			ingredientGrid.setItems(callback.getRepository().findByNameOrInci(searchString));
		}
	}

	private void setItems() {
		filterIngredientByNameOrInci(null);
	}

	public void select(T selectIngredient) {
		ingredientGrid.select(selectIngredient);
	}

	public void deselectAll() {
		ingredientGrid.deselectAll();
	}

	public void refreshAll() {
		setItems();
	}

	public void refresh(T ingredient) {
		setItems();
	}

	void addSelectionListener() {
		SingleSelect<Grid<T>, T> ingredientSelect = ingredientGrid.asSingleSelect();
		ingredientSelect.addValueChangeListener(e -> {
			callback.ingredientSelected(e.getValue());
		});
	}

	public void selectFirstIngredient() {
		List<T> ingredients = ingredientGrid.getListDataView().getItems().collect(Collectors.toList());
		if (ingredients.size() > 0) {
			select(ingredients.get(0));
		} else {
			select(null);
		}
	}

	public Optional<T> getSelectedEntity() {
		SingleSelect<Grid<T>, T> ingredientSelect = ingredientGrid.asSingleSelect();
		return ingredientSelect.getOptionalValue();
	}

	public void listenToSelectionChanges() {
		ingredientGrid.setEnabled(true);
	}

	public void ignoreSelectionChanges() {
		ingredientGrid.setEnabled(false);
	}
}
