package org.soaplab.ui.views;

import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADERNAME_NAME;
import static org.soaplab.ui.pageobjects.RecipeItemTablePageObject.COLUMN_HEADERNAME_PERCENTAGE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceRecipe;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.testdata.RandomSoapRecipeRepositoryTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.soaplab.ui.UIIntegrationTestBase;
import org.soaplab.ui.pageobjects.EntityTablePanelPageObject;
import org.soaplab.ui.pageobjects.FragranceRecipeDetailsPageObject;
import org.soaplab.ui.pageobjects.FragranceRecipeViewPageObject;
import org.soaplab.ui.pageobjects.PageObjectElement;
import org.soaplab.ui.pageobjects.RecipeItemTablePageObject;
import org.soaplab.ui.pageobjects.RowGridObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.codeborne.selenide.Condition;
import com.vaadin.flow.data.binder.ValueContext;

public class FragranceRecipeViewUIIT extends UIIntegrationTestBase {

	private FragranceRecipeViewPageObject pageObject;
	private EntityTablePanelPageObject tablePanel;
	private FragranceRecipeDetailsPageObject detailsPanel;

	@Autowired
	private RepositoryTestHelper repoHelper;

	private RandomSoapRecipeRepositoryTestData testData;
	private FragranceRecipe fragranceRecipe;
	private Fragrance fragrance1;
	private Fragrance fragrance2;

	@BeforeEach
	void beforeEach() {
		if (testData == null) {
			testData = repoHelper.createFragranceRecipe();
			fragranceRecipe = testData.getFragranceRecipe();
			fragrance1 = testData.getFragrance1();
			fragrance2 = testData.getFragrance2();
		}

		selectMenuItem("Fragrance Recipe");
		pageObject = new FragranceRecipeViewPageObject();

		tablePanel = pageObject.getEntityTable();
		tablePanel.row(fragranceRecipe).select();
		detailsPanel = pageObject.getEntityDetails();
	}

	@AfterEach
	void afterEach() {
		pageObject.reset();
	}

	@Test
	void fragranceTableIsShownInDetails() {
		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();

		fragrancesTable.entityShouldAppearInViewPort(fragrance1, fragrance2);
		fragrancesTable.entityShouldNotAppearInViewPort("blub");
	}

	@Test
	void editModeShouldBeEnabledByDoubleClickInFragranceTable() {
		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();

		fragrancesTable.row(fragrance1).doubleClick();

		detailsPanel.shouldBeEditable();
	}

	@Test
	void editorIsClosedIfEscPressedInFragranceTable() {
		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();
		final PageObjectElement editor = fragrancesTable.row(fragrance1).doubleClick();

		editor.pressEscape();

		editor.shouldBeHidden();
	}

	@Test
	void editorIsClosedIfEnterPressedInFragranceTable() {
		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();
		final PageObjectElement editor = fragrancesTable.row(fragrance1).doubleClick();

		editor.pressEnter();

		editor.shouldBeHidden();
	}

	@Test
	void changedFragranceInDetailsPanelShouldBeSaved() {
		final RandomSoapRecipeRepositoryTestData testData2 = repoHelper.createFragranceRecipe();
		final FragranceRecipe fragranceRecipe2 = testData2.getFragranceRecipe();
		final Fragrance fragrance2_1 = testData2.getFragrance1();
		final Fragrance fragrance2_2 = testData2.getFragrance2();
		final Fragrance fragrance3 = repoHelper.createFragrance();
		pageObject.refreshPage();

		tablePanel.row(fragranceRecipe2).select();
		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();
		final PageObjectElement editor = fragrancesTable.row(fragrance2_2).doubleClick(COLUMN_HEADERNAME_NAME);
		editor.setValue(fragrance3.getName());
		// close Combobox
		editor.pressEnter();
		// close editor -> save
		editor.pressEnter();

		detailsPanel.shouldNoBeEditable();
		pageObject.refreshPage();
		tablePanel.row(fragranceRecipe2).select();
		fragrancesTable.entityShouldAppear(fragrance3, fragrance2_1);
		fragrancesTable.entityShouldNotAppear(fragrance2_2);
	}

	@Test
	void changedFragranceInDetailsPanelShouldNotBeSavedWhenEscapePressed() {

		final EntityTablePanelPageObject fragrancesTable = detailsPanel.fragrancesTable();
		final RecipeEntry<Fragrance> recipeEntryFragrance1 = fragranceRecipe.getRecipeEntry(fragrance1);
		final String oldDisplayedPercentageValue = new StringToPercentageConverter()
				.convertToPresentation(recipeEntryFragrance1.getPercentage(), new ValueContext());
		final PageObjectElement editor = fragrancesTable.row(COLUMN_HEADERNAME_PERCENTAGE, oldDisplayedPercentageValue)
				.doubleClick(COLUMN_HEADERNAME_PERCENTAGE);
		editor.setValue("100");
		editor.pressEscape();
		editor.shouldBeHidden();
		fragrancesTable.row(recipeEntryFragrance1.getIngredient()).cellByHeader(COLUMN_HEADERNAME_PERCENTAGE)
				.shouldHave(Condition.text(oldDisplayedPercentageValue));
	}

	@Test
	void addFragrance() {
		final RandomSoapRecipeRepositoryTestData testData2 = repoHelper.createFragranceRecipe();
		final FragranceRecipe fragranceRecipe2 = testData2.getFragranceRecipe();
		final Fragrance fragrance2_1 = testData2.getFragrance1();
		final Fragrance fragrance2_2 = testData2.getFragrance2();
		final Fragrance fragrance3 = repoHelper.createFragrance();
		pageObject.refreshPage();

		tablePanel.row(fragranceRecipe2).select();
		detailsPanel.name().doubleClick();
		final RecipeItemTablePageObject fragrancesTable = detailsPanel.fragrancesTable();
		fragrancesTable.buttonAdd().click();

		final RowGridObject newRow = fragrancesTable.row(COLUMN_HEADERNAME_NAME, "");
		final PageObjectElement editor = newRow.getEditor(COLUMN_HEADERNAME_NAME);
		editor.setValue(fragrance3.getName());
		// close Combobox
		editor.pressEnter();
		final PageObjectElement editor2Percentage = newRow.getEditor(COLUMN_HEADERNAME_PERCENTAGE);
		editor2Percentage.setValue("20");
		editor2Percentage.pressEnter();

		detailsPanel.shouldNoBeEditable();
		pageObject.refreshPage();
		tablePanel.row(fragranceRecipe2).select();
		fragrancesTable.entityShouldAppear(fragrance3, fragrance2_1, fragrance2_2);
	}

	@Test
	void removeFragrance() {
		final RandomSoapRecipeRepositoryTestData testData2 = repoHelper.createFragranceRecipe();
		final FragranceRecipe fragranceRecipe2 = testData2.getFragranceRecipe();
		final Fragrance fragrance2_1 = testData2.getFragrance1();
		final Fragrance fragrance2_2 = testData2.getFragrance2();
		pageObject.refreshPage();

		tablePanel.row(fragranceRecipe2).select();
		detailsPanel.name().doubleClick();
		final RecipeItemTablePageObject fragrancesTable = detailsPanel.fragrancesTable();
		fragrancesTable.row(fragrance2_1).click().shouldBeSelected();
		fragrancesTable.buttonRemove().click();
		detailsPanel.name().pressEnter();

		detailsPanel.shouldNoBeEditable();
		pageObject.refreshPage();
		tablePanel.row(fragranceRecipe2).select();
		fragrancesTable.entityShouldAppear(fragrance2_2);
		fragrancesTable.entityShouldNotAppear(fragrance2_1);
	}

}
