package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Liquid;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.IngredientDetails;

public class AdditiveDetailsPanel extends IngredientDetails<Additive> {

	private static final long serialVersionUID = 1L;

	public AdditiveDetailsPanel(EntityViewDetailsControllerCallback<Additive> callback) {
		super(callback);
	}

}
