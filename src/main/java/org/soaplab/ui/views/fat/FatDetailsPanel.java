package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.ui.views.IngredientDetails;

public class FatDetailsPanel extends IngredientDetails<Fat> {

	private static final long serialVersionUID = 1L;

	public FatDetailsPanel() {
		super();

		addPropertyIntegerField("domain.fat.ins", Fat::getIns, Fat::setIns);
		addPropertyBigDecimalField("domain.ingredient.sapnaoh", Fat::getSapNaoh, Fat::setSapNaoh);
		addPropertyIntegerField("domain.fat.iodine", Fat::getIodine, Fat::setIodine);
		addPropertyIntegerField("domain.fat.lauric", Fat::getLauric, Fat::setLauric);
		addPropertyIntegerField("domain.fat.myristic", Fat::getMyristic, Fat::setMyristic);
		addPropertyIntegerField("domain.fat.palmitic", Fat::getPalmitic, Fat::setPalmitic);
		addPropertyIntegerField("domain.fat.stearic", Fat::getStearic, Fat::setStearic);
		addPropertyIntegerField("domain.fat.ricinoleic", Fat::getRicinoleic, Fat::setRicinoleic);
		addPropertyIntegerField("domain.fat.oleic", Fat::getOleic, Fat::setOleic);
		addPropertyIntegerField("domain.fat.linoleic", Fat::getLinoleic, Fat::setLinoleic);
		addPropertyIntegerField("domain.fat.linolenic", Fat::getLinolenic, Fat::setLinolenic);
	}
}
