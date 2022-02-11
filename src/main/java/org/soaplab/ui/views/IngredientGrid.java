package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class IngredientGrid<T extends Ingredient> extends Grid<T> {

	private static final long serialVersionUID = 1L;

	public IngredientGrid() {
		super();

		addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

		setSelectionMode(SelectionMode.SINGLE);
//		addColumn(T::getId).setHeader(getTranslation("domain.ingredient.id"));
		addColumn(T::getName).setHeader(getTranslation("domain.ingredient.name"));
		addColumn(T::getInci).setHeader(getTranslation("domain.ingredient.inci"));
	}
}
