package org.soaplab.testdata;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Fat;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.utils.IngredientsExampleData;

import lombok.Getter;

@Getter
public class OliveOilSoapBasicRecipeTestData extends SoapRecipeTestDataBuilder {

	private static final String OLIVE_SOAP_RECIPE_NAME = "Basic Olive Soap";

	private Liquid water;
	private NaOH naOH;
	private Fat oliveOil;
	private LyeRecipe lyeRecipe;


	public OliveOilSoapBasicRecipeTestData() {
		super();
	}

	protected Fat createFatOliveOil() {
		return IngredientsExampleData.getOliveOilBuilder().build();
	}

	protected Liquid createLiquidWater() {
		return IngredientsExampleData.getWaterBuilder().build();
	}
	protected NaOH createNaOH() {
		return IngredientsExampleData.getNaOHBuilder().build();
	}

	protected LyeRecipe createLyeRecipe() {
		return createLyeRecipeBuilder().build();
	}

	protected LyeRecipeBuilder<?, ?> createLyeRecipeBuilder() {
		return IngredientsExampleData.getLyRecipeBuilderNaOH() //
				.naOH(createRecipeEntry(naOH, 100d)) //
				.liquids(createRecipeEntries(createRecipeEntry(water, 100d)));
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		oliveOil = createFatOliveOil();
		naOH = createNaOH();
		water = createLiquidWater();
		lyeRecipe = createLyeRecipe();

		return super.getSoapRecipeBuilder() //
				.name(OLIVE_SOAP_RECIPE_NAME) //
				.lyeRecipe(lyeRecipe) //
				.fats(createRecipeEntries(createRecipeEntry(oliveOil, 100d)));
	}
}
