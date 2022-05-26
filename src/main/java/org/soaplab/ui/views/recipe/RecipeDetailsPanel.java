package org.soaplab.ui.views.recipe;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;

public class RecipeDetailsPanel extends EntityDetails<SoapRecipe> {

	private static final long serialVersionUID = 1L;

	public RecipeDetailsPanel(EntityViewDetailsControllerCallback<SoapRecipe> callback) {
		super(callback);
	}

}
