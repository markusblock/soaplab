package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.selection.SingleSelect;

public class IngredientList<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;

	private Grid<T> ingredientGrid;

	private IngredientsViewControllerCallback<T> callback;

	public IngredientList(IngredientsViewControllerCallback<T> callback) {
		super();
		this.callback = callback;

		content = new VerticalLayout();
		add(content);

		HorizontalLayout toolPanel = new HorizontalLayout();
		content.add(toolPanel);

		TextField searchField = new TextField();
		toolPanel.add(searchField);

		Button addButton = new Button();
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(event -> callback.onCreateNewIngredient());
		toolPanel.add(addButton);

		ingredientGrid = new Grid<T>();
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		ingredientGrid.addColumn(T::getName).setHeader(getTranslation("domain.ingredient.name"));
		ingredientGrid.addColumn(T::getInci).setHeader(getTranslation("domain.ingredient.inci"));
		content.add(ingredientGrid);
	}

	public void setItems(CallbackDataProvider<T, Void> dataProvider) {
		ingredientGrid.setItems(dataProvider);
	}

	public void select(T selectIngredient) {
		ingredientGrid.select(selectIngredient);
	}

	public void refreshAll() {
		ingredientGrid.getLazyDataView().refreshAll();
	}

	void addSelectionListener(ValueChangeListener<ValueChangeEvent<T>> listener) {
		SingleSelect<Grid<T>, T> ingredientSelect = ingredientGrid.asSingleSelect();
		ingredientSelect.addValueChangeListener(listener);
	}

	public void selectFirstIngredient() {
		select(ingredientGrid.getLazyDataView().getItem(0));
	}
}
