package org.soaplab.ui.fat;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.testdata.IngredientsRandomTestData;
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
		FatDetailsPageObject details = pageObject.getIngredientDetails();
		details.name().shouldBeReadOnly();
		Fat fat = IngredientsRandomTestData.getFatBuilder().sapNaoh(new BigDecimal(BigInteger.valueOf(1234), 2))
				.build();

		details.buttonAdd().click();
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

		IngredientListPageObject list = pageObject.getIngredientList();
		list.ingredientShouldAppear(fat);
		repoHelper.assertThatFatHasValues(fat.getName(), fat.getInci(), fat.getIns(), fat.getSapNaoh(), fat.getIodine(),
				fat.getLauric(), fat.getLinoleic(), fat.getLinolenic(), fat.getMyristic(), fat.getOleic(),
				fat.getPalmitic(), fat.getRicinoleic(), fat.getStearic());
	}

	@Test
	public void deleteFat() {
		Fat fat1 = repoHelper.createFat();
		Fat fat2 = repoHelper.createFat();

		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.ingredientShouldAppear(fat1).ingredientShouldAppear(fat2);
		list.selectIngredient(fat1);

		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
		details.buttonRemove().click();

		list.ingredientShouldNotAppear(fat1).ingredientShouldAppear(fat2);
		repoHelper.assertThatFatNotExists(getTestName());
	}

	@Test
	public void updateFat() {
		Fat fat1 = repoHelper.createFat();
		Fat fat2 = IngredientsRandomTestData.getFatBuilder().build();

		IngredientListPageObject list = pageObject.getIngredientList();
		list.triggerReload();
		list.ingredientShouldAppear(fat1);
		list.selectIngredient(fat1);

		IngredientDetailsPageObject details = pageObject.getIngredientDetails();
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
