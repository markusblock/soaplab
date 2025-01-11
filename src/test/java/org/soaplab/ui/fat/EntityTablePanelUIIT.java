package org.soaplab.ui.fat;

import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADER_INCI;
import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADER_NAME;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.ui.pageobjects.FatTablePanelPageObject;
import org.soaplab.ui.pageobjects.FatViewPageObject;
import org.soaplab.ui.pageobjects.PageObjectElement;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityTablePanelUIIT extends UIIntegrationTestBase {

	private FatViewPageObject viewPageObject;
	private FatTablePanelPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	private static Fat ingredient1;
	private static Fat ingredient2;
	private static Fat ingredient3;

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
		final PageObjectElement editor = pageObject.doubleClick(ingredient2, COLUMN_HEADER_NAME);

		editor.shouldBeVisible();
	}

	@Test
	void editModeShouldBeEnabledByEnterPressed() {
		final PageObjectElement editor = pageObject.pressEnter(ingredient2);

		editor.shouldBeVisible();
	}

	@Test
	void editModeShouldBeCanceledByEscPressed() {
		final PageObjectElement editor = pageObject.pressEnter(ingredient3);
		editor.shouldBeVisible();

		editor.pressEscape();

		editor.shouldBeHidden();
	}

	@Test
	void editModeShouldBeCanceledByEnterPressed() {
		final PageObjectElement editor = pageObject.pressEnter(ingredient1);
		editor.shouldBeVisible();

		editor.pressEnter();

		editor.shouldBeHidden();
	}

	@Test
	public void enterSavesChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		final PageObjectElement editor = pageObject.doubleClick(ingredient, COLUMN_HEADER_INCI);
		editor.shouldBeVisible();
		editor.setValue("test");

		editor.pressEnter();

		editor.shouldBeHidden();
		pageObject.ingredientPropertyShouldBeDisplayed(ingredient, COLUMN_HEADER_INCI, "test");
	}

	@Test
	public void escDiscardsChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		final PageObjectElement editor = pageObject.doubleClick(ingredient, COLUMN_HEADER_NAME);
		editor.shouldBeVisible();
		editor.setValue("test");

		editor.pressEscape();
		editor.shouldBeHidden();

		pageObject.ingredientShouldAppear(ingredient.getName());
	}

	@Test
	public void searchIngredientFiltersList() {
		final Fat ingredient1 = repoHelper.createFat("abc", "123");
		final Fat ingredient2 = repoHelper.createFat("def", "456");
		final Fat ingredient3 = repoHelper.createFat("cccX", "555");
		viewPageObject.refreshPage();

		// search by name - single result
		pageObject.searchByColumn(COLUMN_HEADER_NAME).setValue(ingredient1.getName());
		pageObject.entityShouldAppearInViewPort(ingredient1);
		pageObject.entityShouldNotAppearInViewPort(ingredient2, ingredient3);

		// reset search by name
		pageObject.clearSearchInColumn(COLUMN_HEADER_NAME);
		pageObject.entityShouldAppear(ingredient1, ingredient2, ingredient3);

		// search by inci - single result
		pageObject.searchByColumn(COLUMN_HEADER_INCI).setValue(ingredient1.getInci());
		pageObject.entityShouldAppearInViewPort(ingredient1);
		pageObject.entityShouldNotAppearInViewPort(ingredient2, ingredient3);
		pageObject.clearSearchInColumn(COLUMN_HEADER_INCI);

		// search by name - multiple result
		pageObject.searchByColumn(COLUMN_HEADER_NAME).setValue("c");
		pageObject.entityShouldAppearInViewPort(ingredient1, ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient2);
		// search while typing
		pageObject.searchByColumn(COLUMN_HEADER_NAME).appendValue("c");
		pageObject.entityShouldAppearInViewPort(ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1, ingredient2);
		pageObject.clearSearchInColumn(COLUMN_HEADER_NAME);

		// search by inci - multiple result
		pageObject.searchByColumn(COLUMN_HEADER_INCI).setValue("5");
		pageObject.entityShouldAppearInViewPort(ingredient2, ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1);
		pageObject.clearSearchInColumn(COLUMN_HEADER_INCI);

		// case insensitive
		pageObject.searchByColumn(COLUMN_HEADER_NAME).setValue("x");
		pageObject.entityShouldAppearInViewPort(ingredient3);
		pageObject.entityShouldNotAppearInViewPort(ingredient1, ingredient2);
		pageObject.clearSearchInColumn(COLUMN_HEADER_NAME);
	}

}
