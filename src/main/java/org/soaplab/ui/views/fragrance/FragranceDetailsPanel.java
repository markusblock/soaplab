package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.ui.views.IngredientDetails;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;

public class FragranceDetailsPanel extends IngredientDetails<Fragrance> {

	private static final long serialVersionUID = 1L;

	public FragranceDetailsPanel(IngredientsViewDetailsControllerCallback<Fragrance> callback) {
		super(callback);
	}

}
