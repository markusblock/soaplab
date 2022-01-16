package org.soaplab.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.CalculatedSoapRecipeResult;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
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

		Assertions.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualTo(Weight.of(12.15, WeightUnit.GRAMS));
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		Assertions
				.assertThat(
						calculatedSoapRecipeResult.getLiquids().get(oliveOilSoapRecipe.getWater().getId()).getWeight())
				.isEqualTo(Weight.of(33, WeightUnit.GRAMS));
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualTo(Weight.of(33, WeightUnit.GRAMS));
	}

	@Test
	void calculateOliveOilSoapRecipe() {
		OliveOilSoapRecipeTestData oliveOilSoapRecipe = new OliveOilSoapRecipeTestData();
		CalculatedSoapRecipeResult calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.getSoapRecipe());

		Assertions.assertThat(calculatedSoapRecipeResult.getNaohTotal())
				.isEqualTo(Weight.of(15.298d, WeightUnit.GRAMS));
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		Assertions
				.assertThat(
						calculatedSoapRecipeResult.getLiquids().get(oliveOilSoapRecipe.getWater().getId()).getWeight())
				.isEqualTo(Weight.of(33, WeightUnit.GRAMS));
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualTo(Weight.of(33, WeightUnit.GRAMS));
	}

}
