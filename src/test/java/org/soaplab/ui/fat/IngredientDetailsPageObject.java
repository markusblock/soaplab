package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;

public class IngredientDetailsPageObject {

	public IngredientDetailsPageObject clickOnAddNewIngredient() {
		VaadinUtils.clickOnElement(byId("ingredientdetails.add"));
		return this;
	}

	public IngredientDetailsPageObject clickOnSaveIngredient() {
		VaadinUtils.clickOnElement(byId("ingredientdetails.save"));
		return this;
	}

	public IngredientDetailsPageObject clickOnRemoveIngredient() {
		VaadinUtils.clickOnElement(byId("ingredientdetails.remove"));
		return this;
	}

	public IngredientDetailsPageObject clickOnEditIngredient() {
		VaadinUtils.clickOnElement(byId("ingredientdetails.edit"));
		return this;
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
