package org.soaplab.service.soapcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceRecipe;
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

		soapRecipe.setWeight(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setCosts(Price.of(0));

		validateSoapRecipeForErros(soapRecipe, issueCollector);

		final LyeRecipe lyeRecipe = soapRecipe.getLyeRecipe();
		final FragranceRecipe fragranceRecipe = soapRecipe.getFragranceRecipe();

		/*
		 * Fats
		 */
		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		// TODO remove and use soapRecipe
		soapRecipe.setFatsCosts(Price.of(0));
		for (final RecipeEntry<Fat> fatentry : soapRecipe.getFats()) {

			calculateIngredientWeight(fatentry, soapRecipe.getFatsWeight())
					.ifPresent(weight -> soapRecipe.setWeight(weightCalc.plus(soapRecipe.getWeight(), weight)));

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
		soapRecipe.setCosts(priceCalc.plus(soapRecipe.getCosts(), soapRecipe.getFatsCosts()));

		/*
		 * Fragrances
		 */
		if (fragranceRecipe != null) {
			final Percentage fragranceTotalPercentage = soapRecipe.getFragranceToFatRatio();
			fragranceRecipe.setWeight(Weight.of(0, WeightUnit.GRAMS));
			fragranceRecipe.setCosts(Price.of(0));
			if (!CollectionUtils.isEmpty(fragranceRecipe.getFragrances())
					&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
				for (final RecipeEntry<Fragrance> fragranceEntry : fragranceRecipe.getFragrances()) {

					calculateIngredientWeight(fragranceEntry,
							weightCalc.calculatePercentage(soapRecipe.getFatsWeight(), fragranceTotalPercentage))
							.ifPresent(weight -> fragranceRecipe
									.setWeight(weightCalc.plus(fragranceRecipe.getWeight(), weight)));

					calculateIngredientPrice(fragranceEntry).ifPresent(
							price -> fragranceRecipe.setCosts(priceCalc.plus(fragranceRecipe.getCosts(), price)));
				}
				// TODO: validate percentage = 100%
				soapRecipe.setWeight(weightCalc.plus(soapRecipe.getWeight(), fragranceRecipe.getWeight()));
				soapRecipe.setCosts(priceCalc.plus(soapRecipe.getCosts(), fragranceRecipe.getCosts()));
			}
		}

		/*
		 * Custom additives
		 */
		soapRecipe.setAdditivesWeight(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setAdditivesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(soapRecipe.getAdditives())) {
			for (final RecipeEntry<Additive> additiveEntry : soapRecipe.getAdditives()) {

				calculateIngredientWeight(additiveEntry, soapRecipe.getFatsWeight()).ifPresent(weight -> soapRecipe
						.setAdditivesWeight(weightCalc.plus(soapRecipe.getAdditivesWeight(), weight)));

				calculateIngredientPrice(additiveEntry).ifPresent(
						price -> soapRecipe.setAdditivesCosts(priceCalc.plus(soapRecipe.getAdditivesCosts(), price)));
			}
			soapRecipe.setWeight(weightCalc.plus(soapRecipe.getWeight(), soapRecipe.getAdditivesWeight()));
			soapRecipe.setCosts(priceCalc.plus(soapRecipe.getCosts(), soapRecipe.getAdditivesCosts()));
		}

		/*
		 * Lye
		 */
		lyeRecipe.setLyeCosts(Price.of(0));
		lyeRecipe.setLyeWeight(Weight.of(0, WeightUnit.GRAMS));

		// Lye Acids
		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		lyeRecipe.setAcidsWeight(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setAcidsCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(lyeRecipe.getAcids())) {
			for (final RecipeEntry<Acid> acidEntry : lyeRecipe.getAcids()) {

				calculateIngredientWeight(acidEntry, soapRecipe.getFatsWeight()).ifPresent(
						weight -> lyeRecipe.setAcidsWeight(weightCalc.plus(lyeRecipe.getAcidsWeight(), weight)));

				calculateIngredientPrice(acidEntry)
						.ifPresent(price -> lyeRecipe.setAcidsCosts(priceCalc.plus(lyeRecipe.getAcidsCosts(), price)));

				final Acid acid = acidEntry.getIngredient();
				final BigDecimal sapNaoh = acid.getSapNaoh();
				final Weight naoh100 = weightCalc.multiply(acidEntry.getWeight(), sapNaoh);
				naohForAcids = weightCalc.plus(naohForAcids, naoh100);
			}
			lyeRecipe.setLyeWeight(weightCalc.plus(lyeRecipe.getLyeWeight(), lyeRecipe.getAcidsWeight()));
			lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), lyeRecipe.getAcidsCosts()));
		}

		// Lye Liquids
		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		lyeRecipe.setLiquidsWeight(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setLiquidsCosts(Price.of(0));
		for (final RecipeEntry<Liquid> liquidEntry : lyeRecipe.getLiquids()) {
			final Liquid liquid = liquidEntry.getIngredient();

			calculateIngredientWeight(liquidEntry,
					weightCalc.calculatePercentage(soapRecipe.getFatsWeight(), soapRecipe.getLiquidToFatRatio()))
					.ifPresent(weight -> lyeRecipe
							.setLiquidsWeight(weightCalc.plus(lyeRecipe.getLiquidsWeight(), weight)));

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
		lyeRecipe.setLyeWeight(weightCalc.plus(lyeRecipe.getLyeWeight(), lyeRecipe.getLiquidsWeight()));

		// Lye Additives
		lyeRecipe.setLyeAdditivesWeight(Weight.of(0, WeightUnit.GRAMS));
		lyeRecipe.setLyeAdditivesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(lyeRecipe.getAdditives())) {
			for (final RecipeEntry<Additive> additiveEntry : lyeRecipe.getAdditives()) {

				calculateIngredientWeight(additiveEntry, soapRecipe.getFatsWeight()).ifPresent(weight -> lyeRecipe
						.setLyeAdditivesWeight(weightCalc.plus(lyeRecipe.getLyeAdditivesWeight(), weight)));

				calculateIngredientPrice(additiveEntry).ifPresent(price -> lyeRecipe
						.setLyeAdditivesCosts(priceCalc.plus(lyeRecipe.getLyeAdditivesCosts(), price)));
			}
			lyeRecipe.setLyeWeight(weightCalc.plus(lyeRecipe.getLyeWeight(), lyeRecipe.getLyeAdditivesWeight()));
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
		lyeRecipe.setNaohWeight(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(naohPercentage)) {
			lyeRecipe.setNaohWeight(weightCalc.calculatePercentage(naohForFatsAndAcidsAndLiquids, naohPercentage));
			lyeRecipe.getNaOH().setWeight(lyeRecipe.getNaohWeight());
			lyeRecipe.setLyeWeight(weightCalc.plus(lyeRecipe.getLyeWeight(), lyeRecipe.getNaohWeight()));

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
		lyeRecipe.setKohWeight(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = lyeRecipe.getKOH().getIngredient().getKohPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			lyeRecipe.setKohWeight(weightCalc.multiply(naohForFatsAndAcidsAndLiquids, naohToKohConversion,
					percentageCalc.divide(kohPercentage, kohPurity)));
			lyeRecipe.getKOH().setWeight(lyeRecipe.getKohWeight());
			lyeRecipe.setLyeWeight(weightCalc.plus(lyeRecipe.getLyeWeight(), lyeRecipe.getKohWeight()));

			calculateIngredientPrice(lyeRecipe.getKOH())
					.ifPresent(price -> lyeRecipe.setLyeCosts(priceCalc.plus(lyeRecipe.getLyeCosts(), price)));
		}

		soapRecipe.setWeight(weightCalc.plus(soapRecipe.getWeight(), lyeRecipe.getLyeWeight()));
		soapRecipe.setCosts(priceCalc.plus(soapRecipe.getCosts(), lyeRecipe.getLyeCosts()));

		soapRecipe.setCostsPer100g(priceCalc.calculatePricePer100g(soapRecipe.getCosts(), soapRecipe.getWeight()));

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
		final Percentage percentage = ingredientEntry.getPercentage();
		final Weight ingredientWeight = weightCalc.calculatePercentage(weightTotal, percentage);
		ingredientEntry.setWeight(ingredientWeight);
		return Optional.of(ingredientWeight);
	}

	private Optional<Price> calculateIngredientPrice(RecipeEntry<? extends Ingredient> ingredientEntry) {
		final Ingredient ingredient = ingredientEntry.getIngredient();
		final Weight ingredientWeight = ingredientEntry.getWeight();

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

		validateAndHandleError(() -> soapRecipe.getFatsWeight() == null, soapRecipe, issueCollector,
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
