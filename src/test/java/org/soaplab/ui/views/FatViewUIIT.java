package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Fat;
import org.soaplab.testdata.RandomIngredientsTestData;
import org.soaplab.ui.RepositoryTestHelper;
import org.soaplab.ui.UIIntegrationTestBase;
import org.soaplab.ui.pageobjects.EntityDetailsPanelPageObject;
import org.soaplab.ui.pageobjects.EntityTablePanelPageObject;
import org.soaplab.ui.pageobjects.FatDetailsPageObject;
import org.soaplab.ui.pageobjects.FatViewPageObject;
import org.springframework.beans.factory.annotation.Autowired;

public class FatViewUIIT extends UIIntegrationTestBase {

	private FatViewPageObject entityView;

	@Autowired
	private RepositoryTestHelper repoHelper;

	@BeforeEach
	void beforeEach() {
		entityView = new FatViewPageObject();
	}

	@AfterEach
	void afterEach() {
		entityView.reset();
	}

	@Test
	public void createNewFat() {
		final FatDetailsPageObject details = entityView.getEntityDetails();
		final EntityTablePanelPageObject table = entityView.getEntityTable();
		final Fat fat = RandomIngredientsTestData.getFatBuilder().sapNaoh(new BigDecimal(BigInteger.valueOf(1234), 2))
				.build();

		entityView.buttonAdd().click();
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
		details.stearic().pressEnter();

		details.id().shouldBeDisabled().shouldNotBeEmpty();
		details.name().shouldBeDisabled().shouldHaveValue(fat.getName());
		details.inci().shouldBeDisabled().shouldHaveValue(fat.getInci());
		details.ins().shouldBeDisabled().shouldHaveValue(fat.getIns());
		details.sapNaoh().shouldBeDisabled().shouldHaveValue(fat.getSapNaoh());
		details.iodine().shouldBeDisabled().shouldHaveValue(fat.getIodine());
		details.lauric().shouldBeDisabled().shouldHaveValue(fat.getLauric());
		details.linoleic().shouldBeDisabled().shouldHaveValue(fat.getLinoleic());
		details.linolenic().shouldBeDisabled().shouldHaveValue(fat.getLinolenic());
		details.myristic().shouldBeDisabled().shouldHaveValue(fat.getMyristic());
		details.oleic().shouldBeDisabled().shouldHaveValue(fat.getOleic());
		details.palmitic().shouldBeDisabled().shouldHaveValue(fat.getPalmitic());
		details.ricinoleic().shouldBeDisabled().shouldHaveValue(fat.getRicinoleic());
		details.stearic().shouldBeDisabled().shouldHaveValue(fat.getStearic());

		table.entityShouldAppear(fat);
		repoHelper.assertThatFatHasSameValuesExceptId(fat);
	}

	@Test
	public void deleteFat() {
		final Fat fat1 = repoHelper.createFat();
		final Fat fat2 = repoHelper.createFat();
		entityView.refreshPage();

		final EntityTablePanelPageObject table = entityView.getEntityTable();
		table.entityShouldAppear(fat1).entityShouldAppear(fat2);
		table.row(fat1).select();

		entityView.buttonRemove().click();

		table.entityShouldNotAppear(fat1).entityShouldAppear(fat2);
		repoHelper.assertThatFatNotExists(getTestName());
	}

	@Test
	public void updateFat() {
		final Fat fat1 = repoHelper.createFat();
		final Fat fat2 = RandomIngredientsTestData.getFatBuilder().build();
		entityView.refreshPage();

		final EntityTablePanelPageObject table = entityView.getEntityTable();
		table.entityShouldAppear(fat1);
		table.row(fat1).select();

		final EntityDetailsPanelPageObject details = entityView.getEntityDetails();
		details.id().shouldBeReadOnly().shouldNotBeEmpty();
		details.name().shouldBeDisabled().shouldNotBeEmpty();

		details.doubleClick();

		details.name().shouldBeEditable().shouldHaveValue(fat1.getName()).setValue(fat2.getName());
		details.inci().shouldBeEditable().shouldHaveValue(fat1.getInci()).setValue(fat2.getInci());
		details.inci().pressEnter();

		details.id().shouldBeReadOnly().shouldHaveValue(fat1.getId().toString());
		details.name().shouldBeDisabled().shouldHaveValue(fat2.getName());
		details.inci().shouldBeDisabled().shouldHaveValue(fat2.getInci());

		table.entityShouldNotAppear(fat1);
		table.entityShouldAppear(fat2);
		repoHelper.assertThatFatExists(fat2.getName(), fat2.getInci());
		repoHelper.assertThatFatNotExists(fat1.getName());

	}

}
