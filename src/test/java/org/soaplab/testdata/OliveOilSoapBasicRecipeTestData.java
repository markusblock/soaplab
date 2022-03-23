package org.soaplab.testdata;

import org.soaplab.domain.Fat;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;

import lombok.Getter;

@Getter
public class OliveOilSoapBasicRecipeTestData extends RecipeTestDataBuilder {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Basic Olive Soap";

	private Fat oliveOil;
	private Liquid water;

	public OliveOilSoapBasicRecipeTestData() {
		super();
	}

	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		oliveOil = IngredientsTestData.getOliveOilBuilder().build();
		water = IngredientsTestData.getWaterBuilder().build();

		return super.getSoapRecipeBuilder() //
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.fats(createIngredientEntriesMap( //
						createRecipeEntry(oliveOil, 100d))) //
				.liquids(createIngredientEntriesMap( //
						createRecipeEntry(water, 100d) //
				));
	}
}
