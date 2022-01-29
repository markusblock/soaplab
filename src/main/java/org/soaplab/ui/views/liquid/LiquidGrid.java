package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.IngredientGrid;

public class LiquidGrid extends IngredientGrid<Liquid> {

	public LiquidGrid() {
		addColumn(Liquid::getSapNaoh).setHeader(getTranslation("domain.ingredient.sapnaoh"));
	}
}
