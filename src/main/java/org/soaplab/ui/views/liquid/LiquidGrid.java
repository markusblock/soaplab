package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.IngredientGrid;

public class LiquidGrid extends IngredientGrid<Liquid> {

	private static final long serialVersionUID = 1L;

	public LiquidGrid() {
		super();

//		addColumn(Liquid::getSapNaoh).setHeader(getTranslation("domain.ingredient.sapnaoh"));
	}
}
