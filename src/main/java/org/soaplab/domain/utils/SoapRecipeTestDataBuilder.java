package org.soaplab.domain.utils;

import java.time.Instant;
import java.util.Date;

import org.soaplab.domain.Percentage;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

import lombok.Getter;

@Getter
public class SoapRecipeTestDataBuilder {

	private final SoapRecipeBuilder<?, ?> soapRecipeBuilder = SoapRecipe.builder();
	private SoapRecipe soapRecipe;

	public SoapRecipeTestDataBuilder() {
		soapRecipeBuilder //
				.manufacturingDate(Date.from(Instant.now())) //
				.fatsTotal(Weight.of(100, WeightUnit.GRAMS)) //
				.liquidToFatRatio(Percentage.of(33)) //
				.superFat(Percentage.of(10)) //
				.fragranceToFatRatio(Percentage.of(3));
	}

	public SoapRecipeBuilder<?, ?> getSoapRecipeBuilder() {
		return soapRecipeBuilder;
	}

	public SoapRecipe createSoapRecipe() {
		soapRecipe = getSoapRecipeBuilder().build();
		return soapRecipe;
	}
}
