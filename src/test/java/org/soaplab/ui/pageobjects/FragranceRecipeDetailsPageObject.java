package org.soaplab.ui.pageobjects;

public class FragranceRecipeDetailsPageObject extends EntityDetailsPanelPageObject {

	public RecipeItemTablePageObject fragrancesTable() {
		return new RecipeItemTablePageObject("recipe.entitylist.grid");
	}

}
