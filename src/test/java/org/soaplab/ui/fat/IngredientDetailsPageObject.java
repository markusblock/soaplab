package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;

public class IngredientDetailsPageObject {

	public PageObjectElement buttonAdd() {
		return new PageObjectElement(byId("ingredientdetails.add"));
	}

	public PageObjectElement buttonSave() {
		return new PageObjectElement(byId("ingredientdetails.save"));
	}

	public PageObjectElement buttonCancel() {
		return new PageObjectElement(byId("ingredientdetails.cancel"));
	}

	public PageObjectElement buttonRemove() {
		return new PageObjectElement(byId("ingredientdetails.remove"));
	}

	public PageObjectElement buttonEdit() {
		return new PageObjectElement(byId("ingredientdetails.edit"));
	}

	public PageObjectElement id() {
		return new PageObjectElement(byId("domain.ingredient.id"));
	}

	public PageObjectElement name() {
		return new PageObjectElement(byId("domain.ingredient.name"));
	}

	public PageObjectElement inci() {
		return new PageObjectElement(byId("domain.ingredient.inci"));
	}
}
