package org.soaplab.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.soaplab.assertions.PriceAssert;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.SoapRecipe.SoapRecipeBuilder;
import org.soaplab.domain.utils.IngredientsExampleData;
import org.soaplab.domain.utils.OliveOilSoapBasicRecipeTestData;
import org.soaplab.domain.utils.OliveOilSoapRecipeTestData;
import org.soaplab.service.soapcalc.SoapCalculatorException;
import org.soaplab.service.soapcalc.SoapCalculatorService;
import org.soaplab.service.soapcalc.SoapCalculatorService.CalculationIssue;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
public class SoapCalculatorServiceTest {

	private SoapCalculatorService calculatorService;

	@Mock
	Environment envMock;

	@BeforeEach
	void beforeTest() {
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
		LyeRecipe calculatedLyeRecipe = calculatedSoapRecipeResult.getLyeRecipe();
		WeightAssert.assertThat(calculatedLyeRecipe.getNaohTotal()).isEqualToWeightInGrams(12.15);
		assertRecipeEntry(calculatedLyeRecipe.getNaOH(), 12.15, 0.09);
		WeightAssert.assertThat(calculatedLyeRecipe.getKohTotal()).isEqualToWeightInGrams(0);
		assertThat(calculatedLyeRecipe.getKOH()).isNull();
		PriceAssert.assertThat(calculatedLyeRecipe.getLyeCosts()).isEqualToValue(0.11);
		WeightAssert.assertThat(calculatedLyeRecipe.getLyeTotal()).isEqualToWeightInGrams(45.15);

		// Lye additives
		Assertions.assertThat(calculatedLyeRecipe.getAdditives()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedLyeRecipe.getLyeAdditivesTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedLyeRecipe.getLyeAdditivesCosts()).isEqualToValue(0);

		// Lye Liquids
		Assertions.assertThat(calculatedLyeRecipe.getLiquids()).hasSize(1);
		assertRecipeEntry(calculatedLyeRecipe.getLiquids().get(0), 33, 0.02);
		WeightAssert.assertThat(calculatedLyeRecipe.getLiquidsTotal()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedLyeRecipe.getLiquidsCosts()).isEqualToValue(0.02);

		// Lye Acids
		Assertions.assertThat(calculatedLyeRecipe.getAcids()).isNullOrEmpty();
		WeightAssert.assertThat(calculatedLyeRecipe.getAcidsTotal()).isEqualToWeightInGrams(0);
		PriceAssert.assertThat(calculatedLyeRecipe.getAcidsCosts()).isEqualToValue(0);

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
		LyeRecipe calculatedLyeRecipe = calculatedSoapRecipeResult.getLyeRecipe();
		WeightAssert.assertThat(calculatedLyeRecipe.getNaohTotal()).isEqualToWeightInGrams(12.5074);
		assertRecipeEntry(calculatedLyeRecipe.getNaOH(), 12.5074, 0.09);
		WeightAssert.assertThat(calculatedLyeRecipe.getKohTotal()).isEqualToWeightInGrams(4.9015);
		assertRecipeEntry(calculatedLyeRecipe.getKOH(), 4.9015, 0.04);
		PriceAssert.assertThat(calculatedLyeRecipe.getLyeCosts()).isEqualToValue(0.23);
		WeightAssert.assertThat(calculatedLyeRecipe.getLyeTotal()).isEqualToWeightInGrams(63.4089);

		// Lye additives
		Assertions.assertThat(calculatedLyeRecipe.getAdditives()).hasSize(2);
		assertRecipeEntry(calculatedLyeRecipe.getAdditives().get(0), 3, 0);
		assertRecipeEntry(calculatedLyeRecipe.getAdditives().get(1), 6, 0.01);
		WeightAssert.assertThat(calculatedLyeRecipe.getLyeAdditivesTotal()).isEqualToWeightInGrams(9);
		PriceAssert.assertThat(calculatedLyeRecipe.getLyeAdditivesCosts()).isEqualToValue(0.01);

		// Lye Liquids
		Assertions.assertThat(calculatedLyeRecipe.getLiquids()).hasSize(2);
		assertRecipeEntry(calculatedLyeRecipe.getLiquids().get(0), 9.9, 0.02);
		assertRecipeEntry(calculatedLyeRecipe.getLiquids().get(1), 23.1, 0.01);
		WeightAssert.assertThat(calculatedLyeRecipe.getLiquidsTotal()).isEqualToWeightInGrams(33);
		PriceAssert.assertThat(calculatedLyeRecipe.getLiquidsCosts()).isEqualToValue(0.03);

		// Lye Acids
		Assertions.assertThat(calculatedLyeRecipe.getAcids()).hasSize(1);
		assertRecipeEntry(calculatedLyeRecipe.getAcids().get(0), 4, 0.06);
		WeightAssert.assertThat(calculatedLyeRecipe.getAcidsTotal()).isEqualToWeightInGrams(4);
		PriceAssert.assertThat(calculatedLyeRecipe.getAcidsCosts()).isEqualToValue(0.06);

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

		// Soap Batter Additives
		Assertions.assertThat(calculatedSoapRecipeResult.getAdditives()).hasSize(1);
		assertRecipeEntry(calculatedSoapRecipeResult.getAdditives().get(0), 1, 0.16);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getAdditivesTotal()).isEqualToWeightInGrams(1);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getAdditivesCosts()).isEqualToValue(0.16);

		// Soaprecipe Totals
		WeightAssert.assertThat(calculatedSoapRecipeResult.getWeightTotal()).isEqualToWeightInGrams(167.4089);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotal()).isEqualToValue(1.99);
		PriceAssert.assertThat(calculatedSoapRecipeResult.getCostsTotalPer100g()).isEqualToValue(1.19);
	}

	private <T extends Ingredient> void assertRecipeEntry(RecipeEntry<T> entry, double weightInGrams, double price) {
		WeightAssert.assertThat(entry.getWeight()).isEqualToWeightInGrams(weightInGrams);
		PriceAssert.assertThat(entry.getPrice()).isEqualToValue(price);
	}

	@Test
	void ensureCalculationNoKOH() {
		final OliveOilSoapBasicRecipeTestData soapRecipeData = new OliveOilSoapBasicRecipeTestData();
		final SoapRecipeBuilder<?, ?> recipeBuilder = soapRecipeData.getSoapRecipeBuilder()
				.lyeRecipe(IngredientsExampleData.getLyeRecipeBuilderNaOH().kOH(null).build());
		final SoapRecipe calculatedSoapRecipeResult = calculatorService.calculate(recipeBuilder.build());
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLyeRecipe().getNaohTotal())
				.isEqualToWeightInGrams(12.15d);
		WeightAssert.assertThat(calculatedSoapRecipeResult.getLyeRecipe().getKohTotal()).isEqualToWeightInGrams(0d);
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

	// TODO: test lye calculation (only NaOH / mixed / only KOH)
	// TODO: test inci summary (also with ingredients without inci)
	// TODO: test calculated soap properties

}
