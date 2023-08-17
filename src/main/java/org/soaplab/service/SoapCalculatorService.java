package org.soaplab.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
import org.soaplab.domain.utils.PercentageCalculator;
import org.soaplab.domain.utils.PriceCalculator;
import org.soaplab.domain.utils.WeightCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.java.Log;

/**
 * Service class for calculating attributes for a soap recipe. Calculated values
 * are Lye, weight, price. The calculated values are stored in the provided
 * {@link SoapRecipe} entity. Values are rounded to 4 decimal places.
 */
// TODO: add business exceptions
@Log
@Component
@PropertySource("classpath:/translation.properties")
public class SoapCalculatorService {

	public enum CalculationIssue {
		FATS_TOTAL_MISSING, NO_FAT_IN_RECIPE
	}

	private static RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static int decimalPlaces = 4;
	private final WeightCalculator weightCalc = new WeightCalculator(decimalPlaces, roundingMode);
	private final PercentageCalculator percentageCalc = new PercentageCalculator(decimalPlaces, roundingMode);
	private final PriceCalculator priceCalc = new PriceCalculator(2, roundingMode);
	private final Environment env;

	@Autowired
	public SoapCalculatorService(Environment env) {
		this.env = env;
	}

	/**
	 * Calculates the values for {@link SoapRecipe}.
	 *
	 * @param soapRecipe the {@link SoapRecipe} with the provided values will be
	 *                   filled with the calculated values.
	 * @return {@link SoapRecipe} with calculated values
	 * @throws SoapCalculatorException An exception is thrown if errors occur in
	 *                                 {@link SoapRecipe} calculation. Warnings will
	 *                                 be returned in {@link SoapRecipe}.
	 */
	public SoapRecipe calculate(final SoapRecipe soapRecipe) {
		final SoapCalculatorIssueCollector issueCollector = new SoapCalculatorIssueCollector();

		soapRecipe.setWeightTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setCostsTotal(Price.of(0));

		validateSoapRecipeForErros(soapRecipe, issueCollector);

		LyeRecipe lyeRecipe = soapRecipe.getLyeRecipe();

		/*
		 * Fats
		 */
		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		// TODO remove and use soapRecipe
		soapRecipe.setFatsCosts(Price.of(0));
		for (final RecipeEntry<Fat> fatentry : soapRecipe.getFats()) {

			calculateIngredientWeight(fatentry, soapRecipe.getFatsTotal()).ifPresent(
					weight -> soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), weight)));

			calculateIngredientPrice(fatentry)
					.ifPresent(price -> soapRecipe.setFatsCosts(priceCalc.plus(soapRecipe.getFatsCosts(), price)));

			final Fat fat = fatentry.getIngredient();
			final BigDecimal sapNaoh = fat.getSapNaoh();
			final Weight naoh100 = weightCalc.multiply(fatentry.getWeight(), sapNaoh);
			final Percentage superFat = soapRecipe.getSuperFat();
			final Weight naohReduction = weightCalc.calculatePercentage(naoh100, superFat);
			final Weight naohPerFat = weightCalc.subtract(naoh100, naohReduction);
			naohForFats = weightCalc.plus(naohForFats, naohPerFat);

			// TODO: validate percentage = 100%
		}
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getFatsCosts()));

		/*
		 * Fragrances
		 */
		final Percentage fragranceTotalPercentage = soapRecipe.getFragranceToFatRatio();
		soapRecipe.setFragrancesTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setFragrancesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(soapRecipe.getFragrances())
				&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
			for (final RecipeEntry<Fragrance> fragranceEntry : soapRecipe.getFragrances()) {

				calculateIngredientWeight(fragranceEntry,
						weightCalc.calculatePercentage(soapRecipe.getFatsTotal(), fragranceTotalPercentage))
						.ifPresent(weight -> soapRecipe
								.setFragrancesTotal(weightCalc.plus(soapRecipe.getFragrancesTotal(), weight)));

				calculateIngredientPrice(fragranceEntry).ifPresent(
						price -> soapRecipe.setFragrancesCosts(priceCalc.plus(soapRecipe.getFragrancesCosts(), price)));
			}
			// TODO: validate percentage = 100%
			soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getFragrancesTotal()));
			soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getFragrancesCosts()));
		}

		/*
		 * Custom additives
		 */
		soapRecipe.setAdditivesTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setAdditivesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(soapRecipe.getAdditives())) {
			for (final RecipeEntry<Additive> additiveEntry : soapRecipe.getAdditives()) {

				calculateIngredientWeight(additiveEntry, soapRecipe.getFatsTotal()).ifPresent(weight -> soapRecipe
						.setAdditivesTotal(weightCalc.plus(soapRecipe.getAdditivesTotal(), weight)));

				calculateIngredientPrice(additiveEntry).ifPresent(
						price -> soapRecipe.setAdditivesCosts(priceCalc.plus(soapRecipe.getAdditivesCosts(), price)));
			}
			soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getAdditivesTotal()));
			soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getAdditivesCosts()));
		}

		/*
		 * Lye
		 */
		lyeRecipe.setLyeCosts(Price.of(0));
		lyeRecipe.setLyeTotal(Weight.of(0, WeightUnit.GRAMS));

		// Lye Acids
		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		lyeRecipe.setAcidsTotal(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setAcidsCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(lyeRecipe.getAcids())) {
			for (final RecipeEntry<Acid> acidEntry : lyeRecipe.getAcids()) {

				calculateIngredientWeight(acidEntry, soapRecipe.getFatsTotal()).ifPresent(
						weight -> lyeRecipe.setAcidsTotal(weightCalc.plus(lyeRecipe.getAcidsTotal(), weight)));

				calculateIngredientPrice(acidEntry)
						.ifPresent(price -> lyeRecipe.setAcidsCosts(priceCalc.plus(lyeRecipe.getAcidsCosts(), price)));

				final Acid acid = acidEntry.getIngredient();
				final BigDecimal sapNaoh = acid.getSapNaoh();
				final Weight naoh100 = weightCalc.multiply(acidEntry.getWeight(), sapNaoh);
				naohForAcids = weightCalc.plus(naohForAcids, naoh100);
			}
			lyeRecipe.setLyeTotal(weightCalc.plus(lyeRecipe.getLyeTotal(), lyeRecipe.getAcidsTotal()));
			lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), lyeRecipe.getAcidsCosts()));
		}

		// Lye Liquids
		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		lyeRecipe.setLiquidsTotal(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setLiquidsCosts(Price.of(0));
		for (final RecipeEntry<Liquid> liquidEntry : lyeRecipe.getLiquids()) {
			final Liquid liquid = liquidEntry.getIngredient();

			calculateIngredientWeight(liquidEntry,
					weightCalc.calculatePercentage(soapRecipe.getFatsTotal(), soapRecipe.getLiquidToFatRatio()))
					.ifPresent(
							weight -> lyeRecipe.setLiquidsTotal(weightCalc.plus(lyeRecipe.getLiquidsTotal(), weight)));

			calculateIngredientPrice(liquidEntry)
					.ifPresent(price -> lyeRecipe.setLiquidsCosts(priceCalc.plus(lyeRecipe.getLiquidsCosts(), price)));

			final BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				final Weight naoh100 = weightCalc.multiply(liquidEntry.getWeight(), sapNaoh);
				naohForLiquids = weightCalc.plus(naohForLiquids, naoh100);
			}
			// TODO: validate percentage = 100%
		}
		lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), lyeRecipe.getLiquidsCosts()));
		lyeRecipe.setLyeTotal(weightCalc.plus(lyeRecipe.getLyeTotal(), lyeRecipe.getLiquidsTotal()));

		// Lye Additives
		lyeRecipe.setLyeAdditivesTotal(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setLyeAdditivesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(lyeRecipe.getAdditives())) {
			for (final RecipeEntry<Additive> additiveEntry : lyeRecipe.getAdditives()) {

				calculateIngredientWeight(additiveEntry, soapRecipe.getFatsTotal()).ifPresent(weight -> lyeRecipe
						.setLyeAdditivesTotal(weightCalc.plus(lyeRecipe.getLyeAdditivesTotal(), weight)));

				calculateIngredientPrice(additiveEntry).ifPresent(price -> lyeRecipe
						.setLyeAdditivesCosts(priceCalc.plus(lyeRecipe.getLyeAdditivesCosts(), price)));
			}
			lyeRecipe.setLyeTotal(weightCalc.plus(lyeRecipe.getLyeTotal(), lyeRecipe.getLyeAdditivesTotal()));
			lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), lyeRecipe.getLyeAdditivesCosts()));
		}

		// NaOH
		final Percentage naohPercentage;
		if (lyeRecipe.getNaOH() == null) {
			naohPercentage = Percentage.of(0d);
		} else {
			naohPercentage = lyeRecipe.getNaOH().getPercentage();
		}

		// TODO: validate koh+naoh=100%
		final Weight naohForFatsAndAcidsAndLiquids = weightCalc.plus(naohForFats, naohForAcids, naohForLiquids);
		lyeRecipe.setNaohTotal(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(naohPercentage)) {
			lyeRecipe.setNaohTotal(weightCalc.calculatePercentage(naohForFatsAndAcidsAndLiquids, naohPercentage));
			lyeRecipe.getNaOH().setWeight(lyeRecipe.getNaohTotal());
			lyeRecipe.setLyeTotal(weightCalc.plus(lyeRecipe.getLyeTotal(), lyeRecipe.getNaohTotal()));

			calculateIngredientPrice(lyeRecipe.getNaOH())
					.ifPresent(price -> lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), price)));
		}

		// KOH
		final Percentage kohPercentage;
		if (lyeRecipe.getKOH() == null) {
			kohPercentage = Percentage.of(0d);
		} else {
			kohPercentage = lyeRecipe.getKOH().getPercentage();
		}
		lyeRecipe.setKohTotal(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = lyeRecipe.getKOH().getIngredient().getKOHPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			lyeRecipe.setKohTotal(weightCalc.multiply(naohForFatsAndAcidsAndLiquids, naohToKohConversion,
					percentageCalc.divide(kohPercentage, kohPurity)));
			lyeRecipe.getKOH().setWeight(lyeRecipe.getKohTotal());
			lyeRecipe.setLyeTotal(weightCalc.plus(lyeRecipe.getLyeTotal(), lyeRecipe.getKohTotal()));

			calculateIngredientPrice(lyeRecipe.getKOH())
					.ifPresent(price -> lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), price)));
		}

		soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), lyeRecipe.getLyeTotal()));
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), lyeRecipe.getLyeCosts()));

		soapRecipe.setCostsTotalPer100g(
				priceCalc.calculatePricePer100g(soapRecipe.getCostsTotal(), soapRecipe.getWeightTotal()));

		if (issueCollector.hasErrors()) {
			throw new SoapCalculatorException(env, soapRecipe, issueCollector);
		}

		return soapRecipe;
	}

	/**
	 * Calculates the weight of the {@link Ingredient} based on the percentage
	 * defined in the {@link RecipeEntry} and sets it to the {@link RecipeEntry} and
	 * also returns it.
	 * 
	 * @param ingredientEntry defines the percentage
	 * @param weightTotal     defines the 100% according to that the weight is
	 *                        calculated
	 * @return the weight of the {@link Ingredient} as {@link Optional}
	 */
	private Optional<Weight> calculateIngredientWeight(RecipeEntry<? extends Ingredient> ingredientEntry,
			Weight weightTotal) {
		Percentage percentage = ingredientEntry.getPercentage();
		final Weight ingredientWeight = weightCalc.calculatePercentage(weightTotal, percentage);
		ingredientEntry.setWeight(ingredientWeight);
		return Optional.of(ingredientWeight);
	}

	private Optional<Price> calculateIngredientPrice(RecipeEntry<? extends Ingredient> ingredientEntry) {
		Ingredient ingredient = ingredientEntry.getIngredient();
		Weight ingredientWeight = ingredientEntry.getWeight();

		if (ingredient.getCost() == null) {
			log.warning("Ignoring price of ingredient " + ingredient + " because price per 100g not set");
			return Optional.empty();
		} else if (ingredientWeight == null) {
			log.warning("Ignoring price of ingredient " + ingredient + " because weight not set");
			return Optional.empty();
		} else {
			final Price ingredientPrice = priceCalc.calculatePriceForWeight(ingredient.getCost(), ingredientWeight);
			ingredientEntry.setPrice(ingredientPrice);
			return Optional.of(ingredientPrice);
		}
	}

	/**
	 * Validates the {@link SoapRecipe} so afterwards the calculation can
	 * technically be done even some information is wrong or missing.
	 *
	 * @param soapRecipe
	 * @param issueCollector
	 */
	private void validateSoapRecipeForErros(SoapRecipe soapRecipe, SoapCalculatorIssueCollector issueCollector) {

		validateAndHandleError(() -> soapRecipe.getFatsTotal() == null, soapRecipe, issueCollector,
				CalculationIssue.FATS_TOTAL_MISSING);

		validateAndHandleError(() -> soapRecipe.getFats() == null, soapRecipe, issueCollector,
				CalculationIssue.NO_FAT_IN_RECIPE);
	}

	private void validateAndHandleError(BooleanSupplier validationFailedCheck, SoapRecipe soapRecipe,
			SoapCalculatorIssueCollector issueCollector, CalculationIssue issue) {
		if (validationFailedCheck.getAsBoolean()) {
			issueCollector.addError(issue);
			throw new SoapCalculatorException(env, soapRecipe, issueCollector);
		}
	}
}
