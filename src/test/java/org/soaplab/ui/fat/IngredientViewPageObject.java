package org.soaplab.ui.fat;

import com.codeborne.selenide.Selenide;

import lombok.Getter;

@Getter
public class IngredientViewPageObject {

	private IngredientListPageObject ingredientList;
	private IngredientDetailsPageObject ingredientDetails;

	public IngredientViewPageObject() {
		ingredientDetails = new IngredientDetailsPageObject();
		ingredientList = new IngredientListPageObject();
	}

	public void refreshPage() {
		Selenide.refresh();
	}

}
