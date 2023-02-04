package org.soaplab.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.SoapRecipe;
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
		final OliveOilSoapBasicRecipeTestData oliveOilSoapRecipe = new OliveOilSoapBasicRecipeTestData();
		final SoapRecipe calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.createSoapRecipe());

		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(12.15);
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(0).getWeight()).isEqualToWeightInGrams(33);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualToWeightInGrams(33);
	}

	@Test
	void calculateOliveOilSoapRecipe() {
		final OliveOilSoapRecipeTestData oliveOilSoapRecipe = new OliveOilSoapRecipeTestData();
		final SoapRecipe calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.createSoapRecipe());

		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(13.7682d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(2.3841d);
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids().size()).isEqualTo(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(0).getWeight()).isEqualToWeightInGrams(33);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidTotal()).isEqualToWeightInGrams(33);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFatsTotal()).isEqualToWeightInGrams(100);
	}

	// TODO: test with 0 KOH
	// TODO: test with no fats
	// TODO: test with no liquid
	// TODO: test with no NaOH

	// TODO: test weight
	// TODO: test price /missing price
	// TODO: test lye calculation (only NaOH / mixed / only KOH)
	// TODO: test inci summary (also with ingredients without inci)
	// TODO: test calculated soap properties

}
