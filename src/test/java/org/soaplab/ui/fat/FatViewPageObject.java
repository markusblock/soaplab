package org.soaplab.ui.fat;

public class FatViewPageObject extends IngredientViewPageObject<IngredientListPageObject, FatDetailsPageObject> {

	private IngredientListPageObject ingredientListPageObject;
	private FatDetailsPageObject fatDetails;

	public FatViewPageObject() {
		ingredientListPageObject = new IngredientListPageObject();
		fatDetails = new FatDetailsPageObject();
	}

	@Override
	public FatDetailsPageObject getIngredientDetails() {
		return fatDetails;
	}

	@Override
	public IngredientListPageObject getIngredientList() {
		return ingredientListPageObject;
	}

}
