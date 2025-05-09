package org.soaplab.ui.views;

import static org.soaplab.testdata.RandomIngredientsTestData.getRandomAlphabeticString;
import static org.soaplab.testdata.RandomIngredientsTestData.getRandomNumericString;
import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADERNAME_INCI;
import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADERNAME_NAME;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.ui.RepositoryTestHelper;
import org.soaplab.ui.UIIntegrationTestBase;
import org.soaplab.ui.pageobjects.FatTablePanelPageObject;
import org.soaplab.ui.pageobjects.FatViewPageObject;
import org.soaplab.ui.pageobjects.PageObjectElement;
import org.springframework.beans.factory.annotation.Autowired;

import com.codeborne.selenide.Condition;

public class EntityTablePanelUIIT extends UIIntegrationTestBase {

	private FatViewPageObject viewPageObject;
	private FatTablePanelPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	private Fat ingredient1;
	private Fat ingredient2;
	private Fat ingredient3;

	@BeforeEach
	void beforeEach() {
		if (ingredient1 == null) {
			ingredient1 = repoHelper.createFat();
			ingredient2 = repoHelper.createFat();
			ingredient3 = repoHelper.createFat();
		}

		viewPageObject = new FatViewPageObject();
		viewPageObject.refreshPage();
		pageObject = viewPageObject.getEntityTable();
	}

	@AfterEach
	void afterEach() {
		viewPageObject.reset();
	}

	@Test
	void editModeShouldBeEnabledByDoubleClick() {
		final PageObjectElement editor = pageObject.row(ingredient2).doubleClick();

		editor.shouldBeVisible();
	}

	@Test
	void editModeShouldBeEnabledByEnterPressed() {
		final PageObjectElement editor = pageObject.row(ingredient2).pressEnter();

		editor.shouldBeVisible();
	}

	@Test
	void editModeShouldBeCanceledByEscPressed() {
		final PageObjectElement editor = pageObject.row(ingredient3).pressEnter();
		editor.shouldBeVisible();

		editor.pressEscape();

		editor.shouldBeHidden();
	}

	@Test
	void editModeShouldBeCanceledByEnterPressed() {
		final PageObjectElement editor = pageObject.row(ingredient1).pressEnter();
		editor.shouldBeVisible();

		editor.pressEnter();

		editor.shouldBeHidden();
	}

	@Test
	public void enterSavesChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		final PageObjectElement editor = pageObject.row(ingredient).doubleClick(COLUMN_HEADERNAME_INCI);
		editor.shouldBeVisible();
		editor.setValue("test");

		editor.pressEnter();

		editor.shouldBeHidden();
		pageObject.row(ingredient).cellByHeader(COLUMN_HEADERNAME_INCI).shouldHave(Condition.text("test"));
	}

	@Test
	public void escDiscardsChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		final PageObjectElement editor = pageObject.row(ingredient).doubleClick();
		editor.shouldBeVisible();
		editor.setValue("test");

		editor.pressEscape();
		editor.shouldBeHidden();

		pageObject.entityShouldAppear(ingredient);
	}

	@Test
	public void searchIngredientFiltersList() {
		final Fat ingredient1 = repoHelper.createFat(getRandomNumericString() + "abc",
				getRandomAlphabeticString() + "123");
		final Fat ingredient2 = repoHelper.createFat(getRandomNumericString() + "def",
				getRandomAlphabeticString() + "456");
		final Fat ingredient3 = repoHelper.createFat(getRandomNumericString() + "cccX",
				getRandomAlphabeticString() + "555");
		viewPageObject.refreshPage();

		// search by name - single result
		pageObject.getColumnFilter(COLUMN_HEADERNAME_NAME).setValue(ingredient1.getName());
		pageObject.entityShouldAppearInViewPort(ingredient1);
		pageObject.entityShouldNotAppearInViewPort(ingredient2, ingredient3);

		// reset search by name
		pageObject.clearColumnFilter(COLUMN_HEADERNAME_NAME);
		pageObject.entityShouldAppear(ingredient1, ingredient2, ingredient3);

		// search by inci - single result
		pageObject.getColumnFilter(COLUMN_HEADERNAME_INCI).setValue(ingredient1.getInci());
		pageObject.entityShouldAppearInViewPort(ingredient1);
		pageObject.entityShouldNotAppearInViewPort(ingredient2, ingredient3);
		pageObject.clearColumnFilter(COLUMN_HEADERNAME_INCI);

		// search by name - multiple result
		pageObject.getColumnFilter(COLUMN_HEADERNAME_NAME).setValue("c");
		pageObject.entityShouldAppearInViewPort(ingredient1, ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient2);
		// search while typing
		pageObject.getColumnFilter(COLUMN_HEADERNAME_NAME).appendValue("c");
		pageObject.entityShouldAppearInViewPort(ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1, ingredient2);
		pageObject.clearColumnFilter(COLUMN_HEADERNAME_NAME);

		// search by inci - multiple result
		pageObject.getColumnFilter(COLUMN_HEADERNAME_INCI).setValue("5");
		pageObject.entityShouldAppearInViewPort(ingredient2, ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1);
		pageObject.clearColumnFilter(COLUMN_HEADERNAME_INCI);

		// case insensitive
		pageObject.getColumnFilter(COLUMN_HEADERNAME_NAME).setValue("x");
		pageObject.entityShouldAppearInViewPort(ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1, ingredient2);
		pageObject.clearColumnFilter(COLUMN_HEADERNAME_NAME);
	}

}
