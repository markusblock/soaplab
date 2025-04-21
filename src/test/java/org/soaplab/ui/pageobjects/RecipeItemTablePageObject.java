package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;

public class RecipeItemTablePageObject extends EntityTablePanelPageObject {

	public static String COLUMN_HEADERNAME_PERCENTAGE = "Percentage %";

	public RecipeItemTablePageObject(String id) {
		super(id);

		addEditorTypeForColumn(COLUMN_HEADERNAME_NAME, "vaadin-combo-box");
	}

	public PageObjectElement buttonAdd() {
		return new PageObjectElement(byId("recipe.entitylist.addRecipeEntry"));
	}

	public PageObjectElement buttonRemove() {
		return new PageObjectElement(byId("recipe.entitylist.removeRecipeEntry"));
	}
}
