package org.soaplab.ui.fat;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.springframework.beans.factory.annotation.Autowired;

public class FatViewUIIT extends UIIntegrationTestBase {

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
	public void createNewFat() {
		final FatDetailsPageObject details = pageObject.getIngredientDetails();
		final IngredientListPageObject list = pageObject.getIngredientList();
		details.name().shouldBeReadOnly();
		final Fat fat = RandomIngredientsTestData.getFatBuilder().sapNaoh(new BigDecimal(BigInteger.valueOf(1234), 2))
				.build();

		list.buttonAdd().click();
		details.id().shouldBeReadOnly().shouldBeEmpty();
		details.name().shouldBeEditable().setValue(fat.getName());
		details.inci().shouldBeEditable().setValue(fat.getInci());
		details.ins().shouldBeEditable().setValue(fat.getIns());
		details.sapNaoh().shouldBeEditable().setValue(fat.getSapNaoh());
		details.iodine().shouldBeEditable().setValue(fat.getIodine());
		details.lauric().shouldBeEditable().setValue(fat.getLauric());
		details.linoleic().shouldBeEditable().setValue(fat.getLinoleic());
		details.linolenic().shouldBeEditable().setValue(fat.getLinolenic());
		details.myristic().shouldBeEditable().setValue(fat.getMyristic());
		details.oleic().shouldBeEditable().setValue(fat.getOleic());
		details.palmitic().shouldBeEditable().setValue(fat.getPalmitic());
		details.ricinoleic().shouldBeEditable().setValue(fat.getRicinoleic());
		details.stearic().shouldBeEditable().setValue(fat.getStearic());
		details.buttonSave().click();

		details.id().shouldBeReadOnly().shouldNotBeEmpty();
		details.name().shouldBeReadOnly().shouldHaveValue(fat.getName());
		details.inci().shouldBeReadOnly().shouldHaveValue(fat.getInci());
		details.ins().shouldBeReadOnly().shouldHaveValue(fat.getIns());
		details.sapNaoh().shouldBeReadOnly().shouldHaveValue(fat.getSapNaoh());
		details.iodine().shouldBeReadOnly().shouldHaveValue(fat.getIodine());
		details.lauric().shouldBeReadOnly().shouldHaveValue(fat.getLauric());
		details.linoleic().shouldBeReadOnly().shouldHaveValue(fat.getLinoleic());
		details.linolenic().shouldBeReadOnly().shouldHaveValue(fat.getLinolenic());
		details.myristic().shouldBeReadOnly().shouldHaveValue(fat.getMyristic());
		details.oleic().shouldBeReadOnly().shouldHaveValue(fat.getOleic());
		details.palmitic().shouldBeReadOnly().shouldHaveValue(fat.getPalmitic());
		details.ricinoleic().shouldBeReadOnly().shouldHaveValue(fat.getRicinoleic());
		details.stearic().shouldBeReadOnly().shouldHaveValue(fat.getStearic());

		list.ingredientShouldAppear(fat);
		repoHelper.assertThatFatHasSameValuesExceptId(fat);
	}

	@Test
	public void deleteFat() {
		final Fat fat1 = repoHelper.createFat();
		final Fat fat2 = repoHelper.createFat();

		final IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.ingredientShouldAppear(fat1).ingredientShouldAppear(fat2);
		list.selectIngredient(fat1);
		list.buttonRemove().click();

		list.ingredientShouldNotAppear(fat1).ingredientShouldAppear(fat2);
		repoHelper.assertThatFatNotExists(getTestName());
	}

	@Test
	public void updateFat() {
		final Fat fat1 = repoHelper.createFat();
		final Fat fat2 = RandomIngredientsTestData.getFatBuilder().build();

		final IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.ingredientShouldAppear(fat1);
		list.selectIngredient(fat1);

		final IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.buttonEdit().click();
		details.id().shouldBeReadOnly().shouldNotBeEmpty();
		details.name().shouldBeEditable().shouldHaveValue(fat1.getName()).setValue(fat2.getName());
		details.inci().shouldBeEditable().shouldHaveValue(fat1.getInci()).setValue(fat2.getInci());
		details.buttonSave().click();

		details.id().shouldBeReadOnly().shouldHaveValue(fat1.getId().toString());
		details.name().shouldBeReadOnly().shouldHaveValue(fat2.getName());
		details.inci().shouldBeReadOnly().shouldHaveValue(fat2.getInci());

		list.ingredientShouldNotAppear(fat1);
		list.ingredientShouldAppear(fat2);
		repoHelper.assertThatFatExists(fat2.getName(), fat2.getInci());
		repoHelper.assertThatFatNotExists(fat1.getName());

	}

}
