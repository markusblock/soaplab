package org.soaplab.testdata;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Fat;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.utils.IngredientsExampleData;

import lombok.Getter;

@Getter
public class OliveOilSoapBasicRecipeTestData extends RecipeTestDataBuilder {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Basic Olive Soap";

	private NaOH naOH;
	private Fat oliveOil;
	private Liquid water;

	public OliveOilSoapBasicRecipeTestData() {
		super();
	}

	protected NaOH createNaOH() {
		return IngredientsExampleData.getNaOHBuilder().build();
	}

	protected Fat createFatOliveOil() {
		return IngredientsExampleData.getOliveOilBuilder().build();
	}

	protected Liquid createLiquidWater() {
		return IngredientsExampleData.getWaterBuilder().build();
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		naOH = createNaOH();
		oliveOil = createFatOliveOil();
		water = createLiquidWater();

		return super.getSoapRecipeBuilder() //
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.naOH(createRecipeEntry(naOH, 100d)) //
				.fats(createRecipeEntries( //
						createRecipeEntry(oliveOil, 100d))) //
				.liquids(createRecipeEntries( //
						createRecipeEntry(water, 100d) //
				));
	}
}
