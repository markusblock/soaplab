package org.soaplab.ui.fat;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static org.soaplab.ui.fat.VaadinUtils.disabled;

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

	public IngredientDetailsPageObject idShouldBeEmpty() {
		$(byId("domain.ingredient.id")).shouldBe(empty);
		return this;
	}

	public IngredientDetailsPageObject idShouldNotBeEmpty() {
		$(byId("domain.ingredient.id")).shouldNotBe(empty);
		return this;
	}

	public IngredientDetailsPageObject idShouldBeReadOnly() {
		$(byId("domain.ingredient.id")).shouldBe(disabled());
		return this;
	}

	public IngredientDetailsPageObject idShouldHaveValue(String value) {
		$(byId("domain.ingredient.id")).$(byTagName("input")).shouldHave(value(value));
		return this;
	}

	public IngredientDetailsPageObject enterIngredientName(String name) {
		$(byId("domain.ingredient.name")).$(byTagName("input")).setValue(name);
		return this;
	}

	public IngredientDetailsPageObject enterIngredientInci(String inci) {
		$(byId("domain.ingredient.inci")).$(byTagName("input")).setValue(inci);
		return this;
	}

	public IngredientDetailsPageObject nameShouldHaveValue(String value) {
		$(byId("domain.ingredient.name")).$(byTagName("input")).shouldHave(value(value));
		return this;
	}

	public IngredientDetailsPageObject inciShouldHaveValue(String value) {
		$(byId("domain.ingredient.inci")).$(byTagName("input")).shouldHave(value(value));
		return this;
	}

	public IngredientDetailsPageObject nameShouldBeReadOnly() {
		$(byId("domain.ingredient.name")).shouldBe(disabled());
		return this;
	}

	public IngredientDetailsPageObject nameShouldBeEditable() {
		$(byId("domain.ingredient.name")).shouldNotBe(disabled());
		return this;
	}

	public IngredientDetailsPageObject inciShouldBeReadOnly() {
		$(byId("domain.ingredient.inci")).shouldBe(disabled());
		return this;
	}

	public IngredientDetailsPageObject inciShouldBeEditable() {
		$(byId("domain.ingredient.inci")).shouldNotBe(disabled());
		return this;
	}
}
