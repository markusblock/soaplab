package org.soaplab.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.soaplab.domain.utils.SoapRecipeUtils.createRecipeEntry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.service.SoapCalculatorService.CalculationIssue;
import org.soaplab.testdata.OliveOilSoapBasicRecipeTestData;
import org.soaplab.testdata.OliveOilSoapRecipeTestData;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
public class SoapCalculatorServiceTest {

	private SoapCalculatorService calculatorService;
	
	@Mock
	Environment envMock;

	@BeforeEach
	private void beforeTest() {
		lenient().when(envMock.getProperty(anyString())).thenReturn("");
		calculatorService = new SoapCalculatorService(envMock);
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
	
	@Test
	void ensureCalculationNoKOH() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		SoapRecipeBuilder<?,?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().kOH(null).naOH(createRecipeEntry(soapRecipeData.getNaOH(), 100d));
		final SoapRecipe calculatedSoapRecipeResult = calculatorService
				.calculate(recipeBuilder.build());
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(12.15d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(0d);
	}
	
	@Test()
	void expectExceptionForRecipeWithNoFat() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		SoapRecipeBuilder<?,?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().fats(null);

		SoapCalculatorException exception = assertThrows(SoapCalculatorException.class, () -> {
			calculatorService.calculate(recipeBuilder.build());
		});

		assertThat(exception.getErrors()).contains(CalculationIssue.NO_FAT_IN_RECIPE);
	}

	@Test()
	void expectExceptionForRecipeWithoutFatsTotal() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		SoapRecipeBuilder<?, ?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().fatsTotal(null);

		SoapCalculatorException exception = assertThrows(SoapCalculatorException.class, () -> {
			calculatorService.calculate(recipeBuilder.build());
		});

		assertThat(exception.getErrors()).contains(CalculationIssue.FATS_TOTAL_MISSING);
	}

	// TODO: test error for no liquid
	// TODO: test error for no Lye
	// TODO: test warning for no price defined on Ingredients
	// TODO: test warning for no KOH and not 100% NaOH
	// TODO: test warning for not 100% Fats

	// TODO: test weight
	// TODO: test price
	// TODO: test lye calculation (only NaOH / mixed / only KOH)
	// TODO: test inci summary (also with ingredients without inci)
	// TODO: test calculated soap properties

}
