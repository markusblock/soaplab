package org.soaplab.ui.views;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.soaplab.ui.UIIntegrationTestBase;
import org.soaplab.ui.pageobjects.FatDetailsPageObject;
import org.soaplab.ui.pageobjects.FatTablePanelPageObject;
import org.soaplab.ui.pageobjects.FatViewPageObject;
import org.soaplab.ui.pageobjects.PageObjectElement;
import org.springframework.beans.factory.annotation.Autowired;

public class IngredientViewUIIT extends UIIntegrationTestBase {

	private FatViewPageObject pageObject;
	private FatTablePanelPageObject tablePanel;
	private FatDetailsPageObject detailsPanel;

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

		pageObject = new FatViewPageObject();
		pageObject.refreshPage();

		tablePanel = pageObject.getEntityTable();
		tablePanel.row(ingredient1).select();
		detailsPanel = pageObject.getEntityDetails();
	}

	@AfterEach
	void afterEach() {
		pageObject.reset();
	}

	@Test
	void detailsPanelShouldShowsSelectedEntityInTablePanel() {
		tablePanel.row(ingredient1).select();
		detailsPanel.name().shouldHaveValue(ingredient1.getName());

		tablePanel.row(ingredient2).select();
		detailsPanel.name().shouldHaveValue(ingredient2.getName());

		tablePanel.row(ingredient3).select();
		detailsPanel.name().shouldHaveValue(ingredient3.getName());
	}

	@Test
	void editModeDisablesButtons() {
		pageObject.buttonAdd().shouldBeEnabled();
		pageObject.buttonRemove().shouldBeEnabled();
		final PageObjectElement editor = tablePanel.row(ingredient2).doubleClick();
		editor.shouldBeVisible();

		pageObject.buttonAdd().shouldBeDisabled();
		pageObject.buttonRemove().shouldBeDisabled();

		editor.pressEscape();
		editor.shouldBeHidden();

		pageObject.buttonAdd().shouldBeEnabled();
		pageObject.buttonRemove().shouldBeEnabled();

		detailsPanel.name().doubleClick();

		pageObject.buttonAdd().shouldBeDisabled();
		pageObject.buttonRemove().shouldBeDisabled();

		detailsPanel.name().pressEscape();

		pageObject.buttonAdd().shouldBeEnabled();
		pageObject.buttonRemove().shouldBeEnabled();
	}

	@Test
	public void updateNameAndInciAreReflectedInList() {
		final Fat ingredient = repoHelper.createFat();
		final Fat updatedValues = RandomIngredientsTestData.getFatBuilder().build();
		pageObject.refreshPage();

		tablePanel.row(ingredient).select();
		detailsPanel.name().doubleClick();
		detailsPanel.name().setValue(updatedValues.getName());
		detailsPanel.inci().setValue(updatedValues.getInci());
		detailsPanel.name().pressEnter();

		tablePanel.entityShouldAppear(updatedValues.getName());
		tablePanel.entityShouldNotAppear(ingredient.getName());
	}
}
