package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;

public class IngredientDetailsPageObject {

	public PageObjectElement buttonAdd() {
		return new PageObjectElement(byId("entitydetails.add"));
	}

	public PageObjectElement buttonSave() {
		return new PageObjectElement(byId("entitydetails.save"));
	}

	public PageObjectElement buttonCancel() {
		return new PageObjectElement(byId("entitydetails.cancel"));
	}

	public PageObjectElement buttonRemove() {
		return new PageObjectElement(byId("entitydetails.remove"));
	}

	public PageObjectElement buttonEdit() {
		return new PageObjectElement(byId("entitydetails.edit"));
	}

	public PageObjectElement id() {
		return new PageObjectElement(byId("domain.entity.id"));
	}

	public PageObjectElement name() {
		return new PageObjectElement(byId("domain.entity.name"));
	}

	public PageObjectElement inci() {
		return new PageObjectElement(byId("domain.ingredient.inci"));
	}

	public void reset() {
		// if in edit mode -> cancel
		if (buttonCancel().isVisible()) {
			buttonCancel().click();
		}

	}
}
