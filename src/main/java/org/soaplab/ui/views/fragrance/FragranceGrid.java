package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.ui.views.IngredientList;

public class FragranceGrid extends IngredientList<Fragrance> {

	private static final long serialVersionUID = 1L;

	public FragranceGrid() {
		super();

//		addColumn(Fragrance::getType).setHeader(getTranslation("domain.fragrance.type"));
	}
}
