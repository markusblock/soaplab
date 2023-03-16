package org.soaplab.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BooleanSupplier;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
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

@Log
@Component
/**
 * Service class for calculating attributes for a soap recipe. Calculated values
 * are Lye, weight, price. The calculated values are stored in the provided
 * {@link SoapRecipe} entity. Values are rounded to 4 decimal places.
 */
// TODO: add business exceptions
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

		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		// TODO remove and use soapRecipe
		Weight totalFatWeight = Weight.of(0, WeightUnit.GRAMS);
		soapRecipe.setFatsCosts(Price.of(0));
		for (final RecipeEntry<Fat> fatentry : soapRecipe.getFats()) {
			final Percentage fatPercentage = fatentry.getPercentage();
			final Fat fat = fatentry.getIngredient();
			final BigDecimal sapNaoh = fat.getSapNaoh();
			final Weight fatsTotal = soapRecipe.getFatsTotal();
			final Weight fatWeight = weightCalc.calculatePercentage(fatsTotal, fatPercentage);
			fatentry.setWeight(fatWeight);
			final Weight naoh100 = weightCalc.multiply(fatWeight, sapNaoh);
			final Percentage superFat = soapRecipe.getSuperFat();
			final Weight naohReduction = weightCalc.calculatePercentage(naoh100, superFat);
			final Weight naohPerFat = weightCalc.subtract(naoh100, naohReduction);

			naohForFats = weightCalc.plus(naohForFats, naohPerFat);
			totalFatWeight = weightCalc.plus(totalFatWeight, fatWeight);

			if (fat.getCost() == null) {
				log.warning("Ignoring price of ingredient " + fat);
			} else {
				final Price fatPrice = priceCalc.calculatePriceForWeight(fat.getCost(), fatWeight);
				fatentry.setPrice(fatPrice);
				soapRecipe.setFatsCosts(priceCalc.plus(soapRecipe.getFatsCosts(), fatPrice));
			}

			// TODO: validate percentage = 100%
		}
		soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), totalFatWeight));
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getFatsCosts()));

		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		soapRecipe.setAcidsTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setAcidsCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(soapRecipe.getAcids())) {
			for (final RecipeEntry<Acid> acidEntry : soapRecipe.getAcids()) {
				final Percentage acidPercentage = acidEntry.getPercentage();
				final Acid acid = acidEntry.getIngredient();
				final BigDecimal sapNaoh = acid.getSapNaoh();
				final Weight fatsTotal = soapRecipe.getFatsTotal();
				final Weight acidWeight = weightCalc.calculatePercentage(fatsTotal, acidPercentage);
				acidEntry.setWeight(acidWeight);
				final Weight naoh100 = weightCalc.multiply(acidWeight, sapNaoh);

				naohForAcids = weightCalc.plus(naohForAcids, naoh100);
				soapRecipe.setAcidsTotal(weightCalc.plus(soapRecipe.getAcidsTotal(), acidWeight));

				if (acid.getCost() == null) {
					log.warning("Ignoring price of ingredient " + acid);
				} else {
					final Price acidPrice = priceCalc.calculatePriceForWeight(acid.getCost(), acidWeight);
					acidEntry.setPrice(acidPrice);
					soapRecipe.setAcidsCosts(priceCalc.plus(soapRecipe.getAcidsCosts(), acidPrice));
				}
			}
		}
		soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getAcidsTotal()));
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getAcidsCosts()));

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		soapRecipe.setLiquidsTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setLiquidsCosts(Price.of(0));
		for (final RecipeEntry<Liquid> liquidEntry : soapRecipe.getLiquids()) {
			final Percentage liquidPercentage = liquidEntry.getPercentage();
			final Liquid liquid = liquidEntry.getIngredient();
			final Weight fatsTotal = soapRecipe.getFatsTotal();
			final Percentage liquidToFatRatio = soapRecipe.getLiquidToFatRatio();
			final Weight liquidWeight = weightCalc.calculatePercentage(fatsTotal, liquidToFatRatio, liquidPercentage);
			liquidEntry.setWeight(liquidWeight);
			soapRecipe.setLiquidsTotal(weightCalc.plus(soapRecipe.getLiquidsTotal(), liquidWeight));

			if (liquid.getCost() == null) {
				log.warning("Ignoring price of ingredient " + liquid);
			} else {
				final Price liquidPrice = priceCalc.calculatePriceForWeight(liquid.getCost(), liquidWeight);
				liquidEntry.setPrice(liquidPrice);
				soapRecipe.setLiquidsCosts(priceCalc.plus(soapRecipe.getLiquidsCosts(), liquidPrice));
			}

			final BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				final Weight naoh100 = weightCalc.multiply(liquidWeight, sapNaoh);
				naohForLiquids = weightCalc.plus(naohForLiquids, naoh100);
			}
			// TODO: validate percentage = 100%
		}
		soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getLiquidsTotal()));
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getLiquidsCosts()));

		final Percentage fragranceTotalPercentage = soapRecipe.getFragranceToFatRatio();
		soapRecipe.setFragrancesTotal(Weight.of(0, WeightUnit.GRAMS));
		soapRecipe.setFragrancesCosts(Price.of(0));
		if (!CollectionUtils.isEmpty(soapRecipe.getFragrances())
				&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
			for (final RecipeEntry<Fragrance> fragranceEntry : soapRecipe.getFragrances()) {
				final Percentage fragrancePercentage = fragranceEntry.getPercentage();
				final Fragrance fragrance = fragranceEntry.getIngredient();
				final Weight fatsTotal = soapRecipe.getFatsTotal();
				final Weight fragranceWeight = weightCalc.calculatePercentage(fatsTotal, fragranceTotalPercentage,
						fragrancePercentage);
				fragranceEntry.setWeight(fragranceWeight);
				soapRecipe.setFragrancesTotal(weightCalc.plus(soapRecipe.getFragrancesTotal(), fragranceWeight));

				if (fragrance.getCost() == null) {
					log.warning("Ignoring price of ingredient " + fragrance);
				} else {
					final Price fragrancePrice = priceCalc.calculatePriceForWeight(fragrance.getCost(),
							fragranceWeight);
					fragranceEntry.setPrice(fragrancePrice);
					soapRecipe.setFragrancesCosts(priceCalc.plus(soapRecipe.getFragrancesCosts(), fragrancePrice));
				}
			}
			// TODO: validate percentage = 100%
		}
		soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getFragrancesTotal()));
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getFragrancesCosts()));

		soapRecipe.setLyeCosts(Price.of(0));
		final Percentage naohPercentage;
		if (soapRecipe.getNaOH() == null) {
			naohPercentage = Percentage.of(0d);
		} else {
			naohPercentage = soapRecipe.getNaOH().getPercentage();
		}
		final Percentage kohPercentage;
		if (soapRecipe.getKOH() == null) {
			kohPercentage = Percentage.of(0d);
		} else {
			kohPercentage = soapRecipe.getKOH().getPercentage();
		}
		// TODO: validate koh+naoh=100%
		final Weight naohForFatsAndAcidsAndLiquids = weightCalc.plus(naohForFats, naohForAcids, naohForLiquids);
		soapRecipe.setNaohTotal(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(naohPercentage)) {
			soapRecipe.setNaohTotal(weightCalc.calculatePercentage(naohForFatsAndAcidsAndLiquids, naohPercentage));
			soapRecipe.getNaOH().setWeight(soapRecipe.getNaohTotal());
			soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getNaohTotal()));

			if (soapRecipe.getNaOH().getIngredient().getCost() == null) {
				log.warning("Ignoring price of ingredient " + soapRecipe.getNaOH().getIngredient());
			} else {
				final Price naOHPrice = priceCalc.calculatePriceForWeight(
						soapRecipe.getNaOH().getIngredient().getCost(), soapRecipe.getNaohTotal());
				soapRecipe.getNaOH().setPrice(naOHPrice);
				soapRecipe.setLyeCosts(priceCalc.plus(soapRecipe.getLyeCosts(), naOHPrice));
			}
		}

		soapRecipe.setKohTotal(Weight.of(0, WeightUnit.GRAMS));
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = soapRecipe.getKOH().getIngredient().getKOHPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			soapRecipe.setKohTotal(weightCalc.multiply(naohForFatsAndAcidsAndLiquids, naohToKohConversion,
					percentageCalc.divide(kohPercentage, kohPurity)));
			soapRecipe.getKOH().setWeight(soapRecipe.getKohTotal());
			soapRecipe.setWeightTotal(weightCalc.plus(soapRecipe.getWeightTotal(), soapRecipe.getKohTotal()));

			if (soapRecipe.getKOH().getIngredient().getCost() == null) {
				log.warning("Ignoring price of ingredient " + soapRecipe.getKOH().getIngredient());
			} else {
				final Price kohPrice = priceCalc.calculatePriceForWeight(soapRecipe.getKOH().getIngredient().getCost(),
						soapRecipe.getKohTotal());
				soapRecipe.getKOH().setPrice(kohPrice);
				soapRecipe.setLyeCosts(priceCalc.plus(soapRecipe.getLyeCosts(), kohPrice));
			}
		}
		soapRecipe.setCostsTotal(priceCalc.plus(soapRecipe.getCostsTotal(), soapRecipe.getLyeCosts()));

		soapRecipe.setCostsTotalPer100g(
				priceCalc.calculatePricePer100g(soapRecipe.getCostsTotal(), soapRecipe.getWeightTotal()));

		if (issueCollector.hasErrors()) {
			throw new SoapCalculatorException(env, soapRecipe, issueCollector);
		}

		return soapRecipe;
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
