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
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.RecipeEntry;
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
		assertRecipeEntry(calculatedSoapRecipeResult.getNaOH(), 12.15, 0.09);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(0);
		assertThat(calculatedSoapRecipeResult.getKOH()).isNull();
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLyeCosts()).isEqualToValue(0.09);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLyeTotal()).isEqualToWeightInGrams(12.15);

		// Liquids
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids()).hasSize(1);
		assertRecipeEntry(calculatedSoapRecipeResult.getLiquids().get(0), 33, 0.02);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidsTotal()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLiquidsCosts()).isEqualToValue(0.02);

		// Acids
		Assertions.assertThat(calculatedSoapRecipeResult.getAcids()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedSoapRecipeResult.getAcidsTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getAcidsCosts()).isEqualToValue(0);

		// Fats
		Assertions.assertThat(calculatedSoapRecipeResult.getFats()).hasSize(1);
		assertRecipeEntry(calculatedSoapRecipeResult.getFats().get(0), 100, 0.61);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFatsTotal()).isEqualToWeightInGrams(100);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFatsCosts()).isEqualToValue(0.61);

		// Fragrances
		Assertions.assertThat(calculatedSoapRecipeResult.getFragrances()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFragrancesTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFragrancesCosts()).isEqualToValue(0);

		// Soaprecipe Totals
		WeightAssert.assertThat(calculatedSoapRecipeResult.getWeightTotal()).isEqualToWeightInGrams(145.15);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotal()).isEqualToValue(0.72);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotalPer100g()).isEqualToValue(0.5);
	}

	@Test
	void calculateOliveOilSoapRecipe() {
		final OliveOilSoapRecipeTestData oliveOilSoapRecipe = new OliveOilSoapRecipeTestData();
		final SoapRecipe calculatedSoapRecipeResult = calculatorService
				.calculate(oliveOilSoapRecipe.createSoapRecipe());

		// Lye
		WeightAssert.assertThat(calculatedSoapRecipeResult.getNaohTotal()).isEqualToWeightInGrams(14.2726);
		assertRecipeEntry(calculatedSoapRecipeResult.getNaOH(), 14.2726, 0.11);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getKohTotal()).isEqualToWeightInGrams(2.4848);
		assertRecipeEntry(calculatedSoapRecipeResult.getKOH(), 2.4848, 0.02);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLyeCosts()).isEqualToValue(0.13);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLyeTotal()).isEqualToWeightInGrams(16.7574);

		// Liquids
		Assertions.assertThat(calculatedSoapRecipeResult.getLiquids()).hasSize(2);
		assertRecipeEntry(calculatedSoapRecipeResult.getLiquids().get(0), 16.5, 0.01);
		assertRecipeEntry(calculatedSoapRecipeResult.getLiquids().get(1), 16.5, 0.03);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLiquidsTotal()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getLiquidsCosts()).isEqualToValue(0.04);

		// Acids
		Assertions.assertThat(calculatedSoapRecipeResult.getAcids()).hasSize(1);
		assertRecipeEntry(calculatedSoapRecipeResult.getAcids().get(0), 4, 0.06);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getAcidsTotal()).isEqualToWeightInGrams(4);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getAcidsCosts()).isEqualToValue(0.06);

		// Fats
		Assertions.assertThat(calculatedSoapRecipeResult.getFats()).hasSize(2);
		assertRecipeEntry(calculatedSoapRecipeResult.getFats().get(0), 80, 0.49);
		assertRecipeEntry(calculatedSoapRecipeResult.getFats().get(1), 20, 0.21);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFatsTotal()).isEqualToWeightInGrams(100);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFatsCosts()).isEqualToValue(0.7);

		// Fragrances
		Assertions.assertThat(calculatedSoapRecipeResult.getFragrances()).hasSize(1);
		assertRecipeEntry(calculatedSoapRecipeResult.getFragrances().get(0), 3, 0.9);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getFragrancesTotal()).isEqualToWeightInGrams(3);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getFragrancesCosts()).isEqualToValue(0.9);

		// Soaprecipe Totals
		WeightAssert.assertThat(calculatedSoapRecipeResult.getWeightTotal()).isEqualToWeightInGrams(156.7574);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotal()).isEqualToValue(1.83);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotalPer100g()).isEqualToValue(1.17);
	}

	private <T extends Ingredient> void assertRecipeEntry(RecipeEntry<T> entry, double weightInGrams, double price) {
		WeightAssert.assertThat(entry.getWeight()).isEqualToWeightInGrams(weightInGrams);
		PriceAssert.assertThat(entry.getPrice()).isEqualToValue(price);
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
