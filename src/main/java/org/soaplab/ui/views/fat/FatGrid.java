package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.ui.views.IngredientGrid;

public class FatGrid extends IngredientGrid<Fat> {

	public FatGrid() {
		addColumn(Fat::getIns).setHeader(getTranslation("domain.fat.ins"));
		addColumn(Fat::getSapNaoh).setHeader(getTranslation("domain.ingredient.sapnaoh"));
		addColumn(Fat::getIodine).setHeader(getTranslation("domain.fat.iodine"));
		addColumn(Fat::getLauric).setHeader(getTranslation("domain.fat.lauric"));
		addColumn(Fat::getMyristic).setHeader(getTranslation("domain.fat.myristic"));
		addColumn(Fat::getPalmitic).setHeader(getTranslation("domain.fat.palmitic"));
		addColumn(Fat::getStearic).setHeader(getTranslation("domain.fat.stearic"));
		addColumn(Fat::getRicinoleic).setHeader(getTranslation("domain.fat.ricinoleic"));
		addColumn(Fat::getOleic).setHeader(getTranslation("domain.fat.oleic"));
		addColumn(Fat::getLinoleic).setHeader(getTranslation("domain.fat.linoleic"));
		addColumn(Fat::getLinolenic).setHeader(getTranslation("domain.fat.linolenic"));
	}
}
