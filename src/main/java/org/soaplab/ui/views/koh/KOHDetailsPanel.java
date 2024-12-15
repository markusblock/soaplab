package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.IngredientDetails;

public class KOHDetailsPanel extends IngredientDetails<KOH> {

	private static final long serialVersionUID = 1L;

	public KOHDetailsPanel(EntityDetailsListener<KOH> callback) {
		super(callback);

		addPropertyPercentageField("domain.koh.kohpurity", KOH::getKohPurity, KOH::setKohPurity);
	}

}
