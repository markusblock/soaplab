package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.ui.views.IngredientGrid;

public class AcidGrid extends IngredientGrid<Acid> {

	public AcidGrid() {
		addColumn(Acid::getSapNaoh).setHeader(getTranslation("domain.ingredient.sapnaoh"));
	}
}
