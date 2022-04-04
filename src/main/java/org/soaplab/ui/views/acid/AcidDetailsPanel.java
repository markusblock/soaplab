package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.ui.views.IngredientDetails;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;

public class AcidDetailsPanel extends IngredientDetails<Acid> {

	private static final long serialVersionUID = 1L;

	public AcidDetailsPanel(IngredientsViewDetailsControllerCallback<Acid> callback) {
		super(callback);

		addPropertyBigDecimalField("domain.ingredient.sapnaoh", Acid::getSapNaoh, Acid::setSapNaoh);
	}

}
