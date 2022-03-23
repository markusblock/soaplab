package org.soaplab.ui.fat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.springframework.beans.factory.annotation.Autowired;

public class FatViewUIIT extends UIIntegrationTestBase {

	private FatViewPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@BeforeEach
	public void beforeEach() {
		pageObject = new FatViewPageObject();
	}

	@Test
	public void createNewFat() {
		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.nameShouldBeReadOnly();

		details.clickOnAddNewIngredient();
		details.idShouldBeEmpty().idShouldBeReadOnly();
		details.nameShouldBeEditable().enterIngredientName("name" + getTestName());
		details.inciShouldBeEditable().enterIngredientInci("inci" + getTestName());
		details.clickOnSaveIngredient();

		details.idShouldNotBeEmpty().idShouldBeReadOnly();
		details.nameShouldBeReadOnly().nameShouldHaveValue("name" + getTestName());
		details.inciShouldBeReadOnly().inciShouldHaveValue("inci" + getTestName());
		IngredientListPageObject list = pageObject.getIngredientList();
		list.ingredientShouldAppear(getTestName());
		repoHelper.assertThatFatExists("name" + getTestName(), "inci" + getTestName());
	}

	@Test
	public void deleteFat() {
		Fat fat1 = repoHelper.createFat();
		Fat fat2 = repoHelper.createFat();
		pageObject.refreshPage();

		IngredientListPageObject list = pageObject.getIngredientList();
		list.ingredientShouldAppear(fat1.getName()).ingredientShouldAppear(fat2.getName());
		list.selectIngredient(fat1);

		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.clickOnRemoveIngredient();

		list.ingredientShouldNotAppear(fat1.getName()).ingredientShouldAppear(fat2.getName());
		repoHelper.assertThatFatNotExists(getTestName());
	}

	@Test
	public void updateFat() {
		Fat fat1 = repoHelper.createFat();
		pageObject.refreshPage();

		IngredientListPageObject list = pageObject.getIngredientList();
		list.ingredientShouldAppear(fat1.getName());
		list.selectIngredient(fat1);

		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.clickOnEditIngredient();
		details.idShouldBeReadOnly().idShouldNotBeEmpty();
		details.nameShouldBeEditable().nameShouldHaveValue(fat1.getName()).enterIngredientName("name" + getTestName());
		details.inciShouldBeEditable().inciShouldHaveValue(fat1.getInci()).enterIngredientInci("inci" + getTestName());
		details.clickOnSaveIngredient();

		details.idShouldBeReadOnly().idShouldHaveValue(fat1.getId().toString());
		details.nameShouldBeReadOnly().nameShouldHaveValue("name" + getTestName());
		details.inciShouldBeReadOnly().inciShouldHaveValue("inci" + getTestName());

		list.ingredientShouldNotAppear(fat1.getName());
		list.ingredientShouldAppear("name" + getTestName());
		repoHelper.assertThatFatExists("name" + getTestName(), "inci" + getTestName());
		repoHelper.assertThatFatNotExists(fat1.getName());

	}

}
