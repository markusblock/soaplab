package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.IngredientDetails;

public class FragranceDetailsPanel extends IngredientDetails<Fragrance> {

	private static final long serialVersionUID = 1L;

	public FragranceDetailsPanel(EntityDetailsListener<Fragrance> callback) {
		super(callback);
	}

}
