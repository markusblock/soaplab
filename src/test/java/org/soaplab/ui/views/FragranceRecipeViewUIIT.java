package org.soaplab.ui.views;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceRecipe;
import org.soaplab.testdata.RandomSoapRecipeRepositoryTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.soaplab.ui.UIIntegrationTestBase;
import org.soaplab.ui.pageobjects.EntityTablePanelPageObject;
import org.soaplab.ui.pageobjects.FragranceRecipeDetailsPageObject;
import org.soaplab.ui.pageobjects.FragranceRecipeViewPageObject;
import org.springframework.beans.factory.annotation.Autowired;

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
			testData = repoHelper.creatFragranceRecipe();
			fragranceRecipe = testData.getFragranceRecipe();
			fragrance1 = testData.getFragrance1();
			fragrance2 = testData.getFragrance2();
		}

		selectMenuItem("Fragrance Recipe");
		pageObject = new FragranceRecipeViewPageObject();

		tablePanel = pageObject.getEntityTable();
		tablePanel.selectEntity(fragranceRecipe);
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

}
