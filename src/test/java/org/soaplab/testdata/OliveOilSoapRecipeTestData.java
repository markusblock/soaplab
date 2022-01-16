package org.soaplab.testdata;

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

	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		oliveOil = IngredientsTestData.getOliveOilBuilder().build();
		water = IngredientsTestData.getWaterBuilder().build();
		coconutOil = IngredientsTestData.getCoconutOilBuilder().build();
		lavendelFragrance = IngredientsTestData.getLavendelFragranceBuilder().build();
		citricAcid = IngredientsTestData.getCitricAcidBuilder().build();
		appleVinegar = IngredientsTestData.getAppleVinegarBuilder().build();

		return super.getSoapRecipeBuilder()//
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.fats(createIngredientEntriesMap( //
						createReceiptEntry(getOliveOil(), 80d), //
						createReceiptEntry(coconutOil, 20d))) //
				.acids(createIngredientEntriesMap( //
						createReceiptEntry(citricAcid, 4d))) //
				.liquids(createIngredientEntriesMap(//
						createReceiptEntry(getWater(), 100d))) //
				// createReceiptEntry(appleVinegar, 50d))) //
				.fragrances(createIngredientEntriesMap( //
						createReceiptEntry(lavendelFragrance, 100d)));
	}
}
