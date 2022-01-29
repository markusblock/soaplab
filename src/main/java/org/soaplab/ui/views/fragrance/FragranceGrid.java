package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.ui.views.IngredientGrid;

public class FragranceGrid extends IngredientGrid<Fragrance> {

	public FragranceGrid() {
		addColumn(Fragrance::getType).setHeader(getTranslation("domain.fragrance.type"));
	}
}
