package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;

public class FatDetailsPageObject extends IngredientDetailsPageObject {

	public PageObjectElement sapNaoh() {
		return new PageObjectElement(byId("domain.ingredient.sapnaoh"));
	}

	public PageObjectElement lauric() {
		return new PageObjectElement(byId("domain.fat.lauric"));
	}

	public PageObjectElement myristic() {
		return new PageObjectElement(byId("domain.fat.myristic"));
	}

	public PageObjectElement palmitic() {
		return new PageObjectElement(byId("domain.fat.palmitic"));
	}

	public PageObjectElement stearic() {
		return new PageObjectElement(byId("domain.fat.stearic"));
	}

	public PageObjectElement ricinoleic() {
		return new PageObjectElement(byId("domain.fat.ricinoleic"));
	}

	public PageObjectElement oleic() {
		return new PageObjectElement(byId("domain.fat.oleic"));
	}

	public PageObjectElement linoleic() {
		return new PageObjectElement(byId("domain.fat.linoleic"));
	}

	public PageObjectElement linolenic() {
		return new PageObjectElement(byId("domain.fat.linolenic"));
	}

	public PageObjectElement iodine() {
		return new PageObjectElement(byId("domain.fat.iodine"));
	}

	public PageObjectElement ins() {
		return new PageObjectElement(byId("domain.fat.ins"));
	}
}
