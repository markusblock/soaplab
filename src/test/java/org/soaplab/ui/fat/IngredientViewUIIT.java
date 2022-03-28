package org.soaplab.ui.fat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;

public class IngredientViewUIIT extends UIIntegrationTestBase {

	private FatViewPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@BeforeEach
	public void beforeEach() {
		pageObject = new FatViewPageObject();
	}

	@Test
	public void editModeEnablesAndDisablesButtons() {
		Fat ingredient = repoHelper.createFat();
		pageObject.refreshPage();
		IngredientListPageObject list = pageObject.getIngredientList();
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		list.selectIngredient(ingredient);
		assertThatButtonAreInNonEditModeWithSelectedIngredient(details);
		details.buttonAdd().click();
		assertThatButtonAreInEditMode(details);
		details.buttonCancel().click();
		assertThatButtonAreInNonEditModeWithSelectedIngredient(details);
		details.buttonEdit().click();
		assertThatButtonAreInEditMode(details);
		details.buttonSave().click();
		assertThatButtonAreInNonEditModeWithSelectedIngredient(details);
	}

	@Test
	public void idReadOnlyInEditAndNonEditMode() {
		Ingredient ingredient = repoHelper.createFat();
		pageObject.refreshPage();
		pageObject.getIngredientList().selectIngredient(ingredient);
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.id().shouldBeReadOnly();
		details.buttonEdit().click();
		details.id().shouldBeReadOnly();
	}

	@Test
	public void nameEditableInEditAndReadOnlyInNonEditMode() {
		Ingredient ingredient = repoHelper.createFat();
		pageObject.refreshPage();
		pageObject.getIngredientList().selectIngredient(ingredient);
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.name().shouldBeReadOnly();
		details.buttonEdit().click();
		details.name().shouldBeEditable();
		details.buttonCancel().click();
		details.name().shouldBeReadOnly();
	}

	@Test
	public void selectionEnablesAndDisablesRemoveAndEditButtons() {
		Fat ingredient = repoHelper.createFat();
		pageObject.refreshPage();
		IngredientListPageObject list = pageObject.getIngredientList();
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		list.selectIngredient(ingredient);
		assertThatButtonAreInNonEditModeWithSelectedIngredient(details);
		list.deSelectIngredient(ingredient);
		assertThatButtonAreInNonEditModeWithNoSelectedIngredient(details);
		list.selectIngredient(ingredient);
		assertThatButtonAreInNonEditModeWithSelectedIngredient(details);
	}

	@Test
	public void detailsOfSelectedIngredientAreShown() {
		Fat ingredient1 = repoHelper.createFat();
		Fat ingredient2 = repoHelper.createFat();
		Fat ingredient3 = repoHelper.createFat();
		pageObject.refreshPage();
		IngredientListPageObject list = pageObject.getIngredientList();
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		list.selectIngredient(ingredient1);
		details.id().shouldHaveValue(ingredient1.getId());
		details.name().shouldHaveValue(ingredient1.getName());
		list.deSelectIngredient(ingredient1);
		details.id().shouldBeEmpty();
		details.name().shouldBeEmpty();
		list.selectIngredient(ingredient2);
		details.id().shouldHaveValue(ingredient2.getId());
		details.name().shouldHaveValue(ingredient2.getName());
		list.selectIngredient(ingredient3);
		details.id().shouldHaveValue(ingredient3.getId());
		details.name().shouldHaveValue(ingredient3.getName());
	}

	private void assertThatButtonAreInEditMode(IngredientDetailsPageObject details) {
		details.buttonSave().shouldBeEnabled();
		details.buttonCancel().shouldBeEnabled();
		details.buttonAdd().shouldBeHidden();
		details.buttonRemove().shouldBeHidden();
		details.buttonEdit().shouldBeHidden();
	}

	private void assertThatButtonAreInNonEditModeWithSelectedIngredient(IngredientDetailsPageObject details) {
		details.buttonSave().shouldBeHidden();
		details.buttonCancel().shouldBeHidden();
		details.buttonAdd().shouldBeEnabled();
		details.buttonRemove().shouldBeEnabled();
		details.buttonEdit().shouldBeEnabled();
	}

	private void assertThatButtonAreInNonEditModeWithNoSelectedIngredient(IngredientDetailsPageObject details) {
		details.buttonSave().shouldBeHidden();
		details.buttonCancel().shouldBeHidden();
		details.buttonAdd().shouldBeEnabled();
		details.buttonRemove().shouldBeDisabled();
		details.buttonEdit().shouldBeDisabled();
	}

}
