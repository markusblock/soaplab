package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.IngredientList;

public class LiquidGrid extends IngredientList<Liquid> {

	private static final long serialVersionUID = 1L;

	public LiquidGrid() {
		super();

//		addColumn(Liquid::getSapNaoh).setHeader(getTranslation("domain.ingredient.sapnaoh"));
	}
}
