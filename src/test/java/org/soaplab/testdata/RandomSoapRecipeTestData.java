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
public class RandomSoapRecipeTestData extends RecipeTestDataBuilder {

	private Fat fat1;
	private Fat fat2;
	private Liquid liquid1;
	private Liquid liquid2;
	private Acid acid1;
	private Acid acid2;
	private Fragrance fragrance1;
	private Fragrance fragrance2;

	public RandomSoapRecipeTestData() {
		super();
	}

	@Override
	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		fat1 = createFat();
		fat2 = createFat();
		liquid1 = createLiquid();
		liquid2 = createLiquid();
		acid1 = createAcid();
		acid2 = createAcid();
		fragrance1 = createFragrance();
		fragrance2 = createFragrance();

		return super.getSoapRecipeBuilder() //
				.name(RandomIngredientsTestData.getRandomString()) //
				.fats(createRecipeEntries(createRecipeEntry(fat1, 60d), createRecipeEntry(fat2, 40d))) //
				.liquids(createRecipeEntries(createRecipeEntry(liquid1, 30d), createRecipeEntry(liquid2, 70d)))
				.acids(createRecipeEntries(createRecipeEntry(acid1, 50d), createRecipeEntry(acid2, 50d))) //
				.fragrances(
						createRecipeEntries(createRecipeEntry(fragrance1, 10d), createRecipeEntry(fragrance2, 90d)));
	}

	protected Fragrance createFragrance() {
		return RandomIngredientsTestData.getFragranceBuilder().build();
	}

	protected Acid createAcid() {
		return RandomIngredientsTestData.getAcidBuilder().build();
	}

	protected Liquid createLiquid() {
		return RandomIngredientsTestData.getLiquidBuilder().build();
	}

	protected Fat createFat() {
		return RandomIngredientsTestData.getFatBuilder().build();
	}
}
