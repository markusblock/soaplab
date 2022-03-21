package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.IngredientDetails;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;

public class LiquidDetailsPanel extends IngredientDetails<Liquid> {

	private static final long serialVersionUID = 1L;

	public LiquidDetailsPanel(IngredientsViewDetailsControllerCallback<Liquid> callback) {
		super(callback);
	}

}
