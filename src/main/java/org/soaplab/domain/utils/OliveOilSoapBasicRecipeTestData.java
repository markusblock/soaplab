package org.soaplab.domain.utils;

import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntries;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.soaplab.domain.Fat;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.LyeRecipe.LyeRecipeBuilder;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;

import lombok.Getter;

@Getter
public class OliveOilSoapBasicRecipeTestData extends SoapRecipeTestDataBuilder {

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
		naOH = createNaOH();
		water = createLiquidWater();
		return IngredientsExampleData.getLyeRecipeBuilderNaOH() //
				.name(IngredientsExampleData.LYE_RECIPE_NAME) //
				.naOH(createRecipeEntry(naOH, 100d)) //
				.liquids(createRecipeEntries(createRecipeEntry(water, 100d)));
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		oliveOil = createFatOliveOil();
		lyeRecipe = createLyeRecipe();

		return super.getSoapRecipeBuilder() //
				.name(IngredientsExampleData.SOAP_RECIPE_NAME) //
				.lyeRecipe(lyeRecipe) //
				.fats(createRecipeEntries(createRecipeEntry(oliveOil, 100d)));
	}
}
