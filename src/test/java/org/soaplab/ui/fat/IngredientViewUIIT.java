package org.soaplab.ui.fat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Ingredient;
import org.soaplab.testdata.IngredientsRandomTestData;
import org.springframework.beans.factory.annotation.Autowired;

public class IngredientViewUIIT extends UIIntegrationTestBase {

	private FatViewPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@BeforeEach
	public void beforeEach() {
		pageObject = new FatViewPageObject();
	}

	@AfterEach
	public void afterEach() {
		pageObject.reset();
	}

	@Test
	public void editModeEnablesAndDisablesButtons() {
		Fat ingredient = repoHelper.createFat();
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
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
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.selectIngredient(ingredient);
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.id().shouldBeReadOnly();
		details.buttonEdit().click();
		details.id().shouldBeReadOnly();
	}

	@Test
	public void nameEditableInEditAndReadOnlyInNonEditMode() {
		Ingredient ingredient = repoHelper.createFat();
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.search().shouldBeVisible();
		list.selectIngredient(ingredient);
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
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
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
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
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

	@Test
	public void updateNameAndInciAreReflectedInList() {
		Fat ingredient = repoHelper.createFat();
		Fat updatedValues = IngredientsRandomTestData.getFatBuilder().build();
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		list.selectIngredient(ingredient);
		details.buttonEdit().click();
		details.name().setValue(updatedValues.getName());
		details.inci().setValue(updatedValues.getInci());
		details.buttonSave().click();

		list.ingredientShouldAppear(updatedValues.getName());
		list.ingredientShouldNotAppear(ingredient.getName());
	}

	@Test
	public void searchIngredientFiltersList() {
		Fat ingredient1 = repoHelper.createFat("abc", "123");
		Fat ingredient2 = repoHelper.createFat("def", "456");
		Fat ingredient3 = repoHelper.createFat("cccX", "555");
		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();

		// search by name - single result
		list.search().setValue(ingredient1.getName());
		list.ingredientShouldAppear(ingredient1);
		list.ingredientShouldNotAppear(ingredient2, ingredient3);

		// search by inci - single result
		list.search().setValue(ingredient1.getInci());
		list.ingredientShouldAppear(ingredient1);
		list.ingredientShouldNotAppear(ingredient2, ingredient3);

		// search by name - multiple result
		list.search().setValue("c");
		list.ingredientShouldAppear(ingredient1, ingredient3);
		list.ingredientShouldNotAppear(ingredient2);
		// search while typing
		list.search().appendValue("c");
		list.ingredientShouldAppear(ingredient3);
		list.ingredientShouldNotAppear(ingredient1, ingredient2);

		// search by inci - multiple result
		list.search().setValue("5");
		list.ingredientShouldAppear(ingredient2, ingredient3);
		list.ingredientShouldNotAppear(ingredient1);

		// case insensitive
		list.search().setValue("x");
		list.ingredientShouldAppear(ingredient3);
		list.ingredientShouldNotAppear(ingredient1, ingredient2);
		// cancel search ESC+button click

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
