package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class IngredientGrid<T extends Ingredient> extends Grid<T> {

	public IngredientGrid() {

		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

		setSelectionMode(SelectionMode.SINGLE);
		addColumn(Ingredient::getId).setHeader("domain.ingredient.id");
		addColumn(Ingredient::getName).setHeader("domain.ingredient.name");
		addColumn(Ingredient::getInci).setHeader("domain.ingredient.inci");
	}

}
