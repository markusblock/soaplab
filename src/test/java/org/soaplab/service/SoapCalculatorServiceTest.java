package org.soaplab.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.CalculatedSoapRecipeResult;
import org.soaplab.testdata.OliveOilSoapBasicRecipeTestData;
import org.soaplab.testdata.OliveOilSoapRecipeTestData;

public class SoapCalculatorServiceTest {

	private SoapCalculatorService calculatorService;

	@BeforeEach
	private void beforeTest() {
		calculatorService = new SoapCalculatorService();
	}

	@Test
	void nullValue() {
		Assertions.assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
			calculatorService.calculate(null);
		});
	}

	@Test
	void calculateBasicOliveOilSoapRecipe() {
		OliveOilSoapBasicRecipeTestData oliveOilSoapRecipe = new OliveOilSoapBasicRecipeTestData();
		CalculatedSoapRecipeResult calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.getSoapRecipe());

		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(12.15);
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		WeightAssert
				.assertThat(
						calculatedSoapRecipeResult.getLiquids().get(oliveOilSoapRecipe.getWater().getId()).getWeight())
				.isEqualToWeightInGrams(33);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualToWeightInGrams(33);
	}

	@Test
	void calculateOliveOilSoapRecipe() {
		OliveOilSoapRecipeTestData oliveOilSoapRecipe = new OliveOilSoapRecipeTestData();
		CalculatedSoapRecipeResult calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.getSoapRecipe());

		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(15.298d);
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		WeightAssert
				.assertThat(
						calculatedSoapRecipeResult.getLiquids().get(oliveOilSoapRecipe.getWater().getId()).getWeight())
				.isEqualToWeightInGrams(33);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualToWeightInGrams(33);
	}

}
