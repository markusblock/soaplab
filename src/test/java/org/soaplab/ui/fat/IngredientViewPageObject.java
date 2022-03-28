package org.soaplab.ui.fat;

import com.codeborne.selenide.Selenide;

public abstract class IngredientViewPageObject<LIST extends IngredientListPageObject, DETAILS extends IngredientDetailsPageObject> {

	public IngredientViewPageObject() {
	}

	public abstract DETAILS getIngredientDetails();

	public abstract LIST getIngredientList();

	public void refreshPage() {
		Selenide.refresh();
	}

}
