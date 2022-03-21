package org.soaplab.ui.views;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.selection.SingleSelect;

public class IngredientList<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;

	private Grid<T> ingredientGrid;

	private IngredientsViewListControllerCallback<T> callback;

	public IngredientList(IngredientsViewListControllerCallback<T> callback) {
		super();
		this.callback = callback;

		content = new VerticalLayout();
		add(content);

		HorizontalLayout toolPanel = new HorizontalLayout();
		content.add(toolPanel);

		TextField searchField = new TextField();
		toolPanel.add(searchField);

		ingredientGrid = new Grid<T>();
		ingredientGrid.setId("ingredientlist.grid");
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addColumn(T::getName).setHeader(getTranslation("domain.ingredient.name"));
		ingredientGrid.addColumn(T::getInci).setHeader(getTranslation("domain.ingredient.inci"));
		content.add(ingredientGrid);

		addSelectionListener();
	}

	public void setItems(CallbackDataProvider<T, Void> dataProvider) {
		ingredientGrid.setItems(dataProvider);
	}

	public void select(T selectIngredient) {
		ingredientGrid.select(selectIngredient);
	}

	public void deselectAll() {
		ingredientGrid.deselectAll();
	}

	public void refreshAll() {
		ingredientGrid.getLazyDataView().refreshAll();
	}

	public void refresh(T ingredient) {
		ingredientGrid.getLazyDataView().refreshItem(ingredient);
	}

	void addSelectionListener() {
		SingleSelect<Grid<T>, T> ingredientSelect = ingredientGrid.asSingleSelect();
		ingredientSelect.addValueChangeListener(e -> {
			callback.ingredientSelected(e.getValue());
		});
	}

	public void selectFirstIngredient() {
		List<T> ingredients = ingredientGrid.getLazyDataView().getItems().collect(Collectors.toList());
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
