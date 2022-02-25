package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.IngredientDetails;
import org.soaplab.ui.views.IngredientsViewControllerCallback;

public class LiquidDetailsPanel extends IngredientDetails<Liquid> {

	private static final long serialVersionUID = 1L;

	public LiquidDetailsPanel(IngredientsViewControllerCallback<Liquid> callback) {
		super(callback);
	}

}
