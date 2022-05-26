package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.IngredientDetails;

public class AcidDetailsPanel extends IngredientDetails<Acid> {

	private static final long serialVersionUID = 1L;

	public AcidDetailsPanel(EntityViewDetailsControllerCallback<Acid> callback) {
		super(callback);

		addPropertyBigDecimalField("domain.ingredient.sapnaoh", Acid::getSapNaoh, Acid::setSapNaoh);
	}

}
