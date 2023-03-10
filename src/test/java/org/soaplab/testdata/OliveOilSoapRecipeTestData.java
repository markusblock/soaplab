package org.soaplab.testdata;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.utils.IngredientsExampleData;

import lombok.Getter;

@Getter
public class OliveOilSoapRecipeTestData extends OliveOilSoapBasicRecipeTestData {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Olive Soap";
	private KOH kOH;
	private Acid citricAcid;
	private Liquid appleVinegar;
	private Fat coconutOil;
	private Fragrance lavendelFragrance;

	public OliveOilSoapRecipeTestData() {
		super();
	}

	protected Liquid createAcidAppleVinegar() {
		return IngredientsExampleData.getAppleVinegarBuilder().build();
	}

	protected Acid createAcidCitric() {
		return IngredientsExampleData.getCitricAcidBuilder().build();
	}

	protected Fat createFatCoconutOil() {
		return IngredientsExampleData.getCoconutOilBuilder().build();
	}

	protected Fragrance createFragranceLavendel() {
		return IngredientsExampleData.getLavendelFragranceBuilder().build();
	}

	protected KOH createKOH() {
		return IngredientsExampleData.getKOHBuilder().build();
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		kOH = createKOH();
		coconutOil = createFatCoconutOil();
		lavendelFragrance = createFragranceLavendel();
		citricAcid = createAcidCitric();
		appleVinegar = createAcidAppleVinegar();

		return super.getSoapRecipeBuilder()//
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.naOH(createRecipeEntry(getNaOH(), 90d)) //
				.kOH(createRecipeEntry(kOH, 10d)) //
				.fats(createRecipeEntries( //
						createRecipeEntry(getOliveOil(), 80d), //
						createRecipeEntry(coconutOil, 20d))) //
				.acids(createRecipeEntries( //
						createRecipeEntry(citricAcid, 4d))) //
				.liquids(createRecipeEntries( //
						createRecipeEntry(getWater(), 50d), //
						createRecipeEntry(appleVinegar, 50d))) //
				.fragrances(createRecipeEntries( //
						createRecipeEntry(lavendelFragrance, 100d)));
	}
}
