package org.soaplab.testdata;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;

import lombok.Getter;

@Getter
public class OliveOilSoapRecipeTestData extends RecipeTestDataBuilder {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Olive Soap";
	private Fat oliveOil;
	private Liquid water;
	private Acid citricAcid;
	private Liquid appleVinegar;
	private Fat coconutOil;
	private Fragrance lavendelFragrance;

	public OliveOilSoapRecipeTestData() {
		super();
	}

	protected Liquid createAcidAppleVinegar() {
		return IngredientsTestData.getAppleVinegarBuilder().build();
	}

	protected Acid createAcidCitric() {
		return IngredientsTestData.getCitricAcidBuilder().build();
	}

	protected Fat createFatCoconutOil() {
		return IngredientsTestData.getCoconutOilBuilder().build();
	}

	protected Fat createFatOliveOil() {
		return IngredientsTestData.getOliveOilBuilder().build();
	}

	protected Fragrance createFragranceLavendel() {
		return IngredientsTestData.getLavendelFragranceBuilder().build();
	}

	protected Liquid createLiquidWater() {
		return IngredientsTestData.getWaterBuilder().build();
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		oliveOil = createFatOliveOil();
		water = createLiquidWater();
		coconutOil = createFatCoconutOil();
		lavendelFragrance = createFragranceLavendel();
		citricAcid = createAcidCitric();
		appleVinegar = createAcidAppleVinegar();

		return super.getSoapRecipeBuilder()//
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.fats(createRecipeEntries( //
						createRecipeEntry(oliveOil, 80d), //
						createRecipeEntry(coconutOil, 20d))) //
				.acids(createRecipeEntries( //
						createRecipeEntry(citricAcid, 4d))) //
				.liquids(createRecipeEntries(//
						createRecipeEntry(water, 100d))) //
				// createReceiptEntry(appleVinegar, 50d))) //
				.fragrances(createRecipeEntries( //
						createRecipeEntry(lavendelFragrance, 100d)));
	}
}
