package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;

public class FatDetailsPageObject extends EntityDetailsPanelPageObject {

	public PageObjectElement sapNaoh() {
		return new PageObjectElement(byId("entitydetailspanel.domain.ingredient.sapnaoh"));
	}

	public PageObjectElement lauric() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.lauric"));
	}

	public PageObjectElement myristic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.myristic"));
	}

	public PageObjectElement palmitic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.palmitic"));
	}

	public PageObjectElement stearic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.stearic"));
	}

	public PageObjectElement ricinoleic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.ricinoleic"));
	}

	public PageObjectElement oleic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.oleic"));
	}

	public PageObjectElement linoleic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.linoleic"));
	}

	public PageObjectElement linolenic() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.linolenic"));
	}

	public PageObjectElement iodine() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.iodine"));
	}

	public PageObjectElement ins() {
		return new PageObjectElement(byId("entitydetailspanel.domain.fat.ins"));
	}
}
