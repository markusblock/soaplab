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

	private final SoapRecipeBuilder<?, ?> soapRecipeBuilder = SoapRecipe.builder();

	public RecipeTestDataBuilder() {
		soapRecipeBuilder.id(UUID.randomUUID()).manufacturingDate(Date.from(Instant.now()))
				.fatsTotal(Weight.of(100, WeightUnit.GRAMS)) //
				.liquidToFatRatio(Percentage.of(33)) //
				.superFat(Percentage.of(10)) //
				.fragranceToFatRatio(Percentage.of(3));
	}

	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		return soapRecipeBuilder;
	}

	public SoapRecipe createSoapRecipe() {
		return getSoapRecipeBuilder().build();
	}
}
