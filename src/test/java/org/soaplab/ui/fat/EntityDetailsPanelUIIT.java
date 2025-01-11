package org.soaplab.ui.fat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.ui.pageobjects.EntityDetailsPanelPageObject;
import org.soaplab.ui.pageobjects.FatViewPageObject;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityDetailsPanelUIIT extends UIIntegrationTestBase {

	private FatViewPageObject viewPageObject;
	private EntityDetailsPanelPageObject pageObject;

	@Autowired
	private RepositoryTestHelper repoHelper;

	private static Fat ingredient1;

	@BeforeEach
	void beforeEach() {
		if (ingredient1 == null) {
			ingredient1 = repoHelper.createFat();
		}

		viewPageObject = new FatViewPageObject();
		viewPageObject.refreshPage();
		viewPageObject.getEntityTable().selectIngredient(ingredient1);
		pageObject = viewPageObject.getEntityDetails();
		pageObject.name().shouldHaveValue(ingredient1.getName());
	}

	@AfterEach
	void afterEach() {
		viewPageObject.reset();
	}

	@Test
	void editModeShouldBeEnabledByDoubleClick() {
		pageObject.name().doubleClick();

		pageObject.name().shouldBeEditable();
	}

	@Test
	void editModeShouldBeCanceledByEscPressed() {
		pageObject.name().doubleClick().shouldBeEditable();

		pageObject.name().pressEscape();

		pageObject.name().shouldBeDisabled();
	}

	@Test
	void editModeShouldBeCanceledByEnterPressed() {
		pageObject.name().doubleClick().shouldBeEditable();

		pageObject.name().pressEnter();

		pageObject.name().shouldBeDisabled();
	}

	@Test
	public void idReadOnlyInEditAndNonEditMode() {
		// readonly mode
		pageObject.id().shouldBeDisabled().shouldBeReadOnly();

		// edit mode
		pageObject.id().doubleClick();
		pageObject.id().shouldBeDisabled().shouldBeReadOnly();

		// readonly mode
		pageObject.name().pressEscape();
		pageObject.id().shouldBeDisabled().shouldBeReadOnly();
	}

	@Test
	public void nameEditableInEditAndReadOnlyInNonEditMode() {
		// readonly mode
		pageObject.name().shouldBeDisabled();

		// edit mode
		pageObject.name().doubleClick();
		pageObject.name().shouldBeEditable();

		// readonly mode
		pageObject.name().pressEscape();
		pageObject.name().shouldBeDisabled();
	}

	@Test
	public void enterSavesChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		viewPageObject.getEntityTable().selectIngredient(ingredient);
		pageObject.name().shouldHaveValue(ingredient.getName());
		pageObject.name().doubleClick().shouldBeEditable();
		pageObject.name().setValue("test");

		pageObject.name().pressEnter();

		pageObject.name().shouldBeDisabled().shouldHaveValue("test");
	}

	@Test
	public void escDiscardsChanges() {
		final Fat ingredient = repoHelper.createFat();
		viewPageObject.refreshPage();
		viewPageObject.getEntityTable().selectIngredient(ingredient);
		pageObject.name().shouldHaveValue(ingredient.getName());
		pageObject.name().doubleClick().shouldBeEditable();
		pageObject.name().setValue("test");

		pageObject.name().pressEscape();

		pageObject.name().shouldBeDisabled().shouldHaveValue(ingredient.getName());
	}

}
