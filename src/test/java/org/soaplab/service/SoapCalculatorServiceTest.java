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
import org.soaplab.assertions.PriceAssert;
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

		// Lye
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(12.15);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaOH().getWeight()).isEqualToWeightInGrams(12.15);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getNaOH().getPrice()).isEqualToValue(0.911d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(0);
		assertThat(calculatedSoapRecipeResult.getKOH()).isNull();
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLyeCosts()).isEqualToValue(0.911d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLyeTotal()).isEqualToWeightInGrams(12.15);

		// Liquids
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids()).hasSize(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(0).getWeight()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(0).getPrice()).isEqualToValue(0.0165);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidsTotal()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLiquidsCosts()).isEqualToValue(0.0165);

		// Acids
		Assertions.assertThat(calculatedSoapRecipeResult.getAcids()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedSoapRecipeResult.getAcidsTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getAcidsCosts()).isEqualToValue(0);

		// Fats
		Assertions.assertThat(calculatedSoapRecipeResult.getFats()).hasSize(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFats().get(0).getWeight()).isEqualToWeightInGrams(100);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFats().get(0).getPrice()).isEqualToValue(0.61);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFatsTotal()).isEqualToWeightInGrams(100);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFatsCosts()).isEqualToValue(0.61);

		// Fragrances
		Assertions.assertThat(calculatedSoapRecipeResult.getFragrances()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFragrancesTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFragrancesCosts()).isEqualToValue(0);

		// Soaprecipe Totals
		WeightAssert.assertThat(calculatedSoapRecipeResult.getWeightTotal()).isEqualToWeightInGrams(145.15);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotal()).isEqualToValue(0.7176d);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotalPer100g()).isEqualToValue(0.5d);
	}

	@Test
	void calculateOliveOilSoapRecipe() {
		final OliveOilSoapRecipeTestData oliveOilSoapRecipe = new OliveOilSoapRecipeTestData();
		final SoapRecipe calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.createSoapRecipe());

		// Lye
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(14.2726d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(2.4848d);

		// Liquids
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids()).hasSize(2);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(0).getWeight())
				.isEqualToWeightInGrams(16.5d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquids().get(1).getWeight())
				.isEqualToWeightInGrams(16.5d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidsTotal()).isEqualToWeightInGrams(33);

		// Fats
		Assertions.assertThat(calculatedSoapRecipeResult.getFats()).hasSize(2);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFats().get(0).getWeight()).isEqualToWeightInGrams(80d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFats().get(1).getWeight()).isEqualToWeightInGrams(20d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFatsTotal()).isEqualToWeightInGrams(100);

		// Fragrances
		Assertions.assertThat(calculatedSoapRecipeResult.getFragrances()).hasSize(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFragrances().get(0).getWeight())
				.isEqualToWeightInGrams(3d);

		// Acids
		Assertions.assertThat(calculatedSoapRecipeResult.getAcids()).hasSize(1);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getAcids().get(0).getWeight()).isEqualToWeightInGrams(4d);

		// Totals
		WeightAssert.assertThat(calculatedSoapRecipeResult.getWeightTotal()).isEqualToWeightInGrams(156.7574);
	}

	@Test
	void ensureCalculationNoKOH() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		final SoapRecipeBuilder<?, ?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().kOH(null)
				.naOH(createRecipeEntry(soapRecipeData.getNaOH(), 100d));
		final SoapRecipe calculatedSoapRecipeResult = calculatorService.calculate(recipeBuilder.build());
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(12.15d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(0d);
	}

	@Test()
	void expectExceptionForRecipeWithNoFat() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		final SoapRecipeBuilder<?, ?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().fats(null);

		final SoapCalculatorException exception = assertThrows(SoapCalculatorException.class, () -> {
			calculatorService.calculate(recipeBuilder.build());
		});

		assertThat(exception.getErrors()).contains(CalculationIssue.NO_FAT_IN_RECIPE);
	}

	@Test()
	void expectExceptionForRecipeWithoutFatsTotal() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		final SoapRecipeBuilder<?, ?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder().fatsTotal(null);

		final SoapCalculatorException exception = assertThrows(SoapCalculatorException.class, () -> {
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
