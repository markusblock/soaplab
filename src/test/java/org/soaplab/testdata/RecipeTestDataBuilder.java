package org.soaplab.testdata;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.soaplab.domain.Percentage;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

import lombok.Getter;

@Getter
public class RecipeTestDataBuilder {

	private SoapRecipeBuilder<?, ?> soapRecipeBuilder = SoapRecipe.builder();
	private IngredientsTestData ingredients;

	public RecipeTestDataBuilder() {
		ingredients = new IngredientsTestData();
		soapRecipeBuilder.id(UUID.randomUUID()).manufacturingDate(Date.from(Instant.now()))
				.naOHToKOHRatio(Percentage.of(100)).fatsTotal(Weight.of(100, WeightUnit.GRAMS))
				.liquidToFatRatio(Percentage.of(33)).superFat(Percentage.of(10)).fragranceTotal(Percentage.of(3));
	}

	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		return soapRecipeBuilder;
	}

	public SoapRecipe createSoapRecipe() {
		return getSoapRecipeBuilder().build();
	}
}
