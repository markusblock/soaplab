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
		FATS_TOTAL_MISSING,
		NO_FAT_IN_RECIPE
	}

	private static RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static int decimalPlaces = 4;
	private final WeightCalculator weightCalc = new WeightCalculator(decimalPlaces, roundingMode);
	private final PercentageCalculator percentageCalc = new PercentageCalculator(decimalPlaces, roundingMode);
	private final PriceCalculator priceCalc = new PriceCalculator(decimalPlaces, roundingMode);
	private Environment env;

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
		SoapCalculatorIssueCollector issueCollector = new SoapCalculatorIssueCollector();

		Weight totalWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalCost = Price.of(0);
		validateSoapRecipeForErros(soapRecipe, issueCollector);

		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		Weight totalFatWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalFatCosts = Price.of(0);
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
				totalFatCosts = priceCalc.plus(totalFatCosts, fatPrice);
			}

			// TODO: validate percentage = 100%
		}
		totalWeight = weightCalc.plus(totalWeight, totalFatWeight);
		totalCost = priceCalc.plus(totalCost, totalFatCosts);

		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		Weight totalAcidWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalAcidCosts = Price.of(0);
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
				totalAcidWeight = weightCalc.plus(totalFatWeight, acidWeight);

				if (acid.getCost() == null) {
					log.warning("Ignoring price of ingredient " + acid);
				} else {
					final Price acidPrice = priceCalc.calculatePriceForWeight(acid.getCost(), acidWeight);
					acidEntry.setPrice(acidPrice);
					totalAcidCosts = priceCalc.plus(totalAcidCosts, acidPrice);
				}
			}
		}
		totalWeight = weightCalc.plus(totalWeight, totalAcidWeight);
		totalCost = priceCalc.plus(totalCost, totalAcidCosts);

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		Weight totalLiquidWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalLiquidCosts = Price.of(0);
		for (final RecipeEntry<Liquid> liquidEntry : soapRecipe.getLiquids()) {
			final Percentage liquidPercentage = liquidEntry.getPercentage();
			final Liquid liquid = liquidEntry.getIngredient();
			final Weight fatsTotal = soapRecipe.getFatsTotal();
			final Percentage liquidToFatRatio = soapRecipe.getLiquidToFatRatio();
			final Weight liquidWeight = weightCalc.calculatePercentage(fatsTotal, liquidToFatRatio, liquidPercentage);
			liquidEntry.setWeight(liquidWeight);
			totalLiquidWeight = weightCalc.plus(totalLiquidWeight, liquidWeight);

			if (liquid.getCost() == null) {
				log.warning("Ignoring price of ingredient " + liquid);
			} else {
				final Price liquidPrice = priceCalc.calculatePriceForWeight(liquid.getCost(), liquidWeight);
				liquidEntry.setPrice(liquidPrice);
				totalLiquidCosts = priceCalc.plus(totalLiquidCosts, liquidPrice);
			}

			final BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				final Weight naoh100 = weightCalc.multiply(liquidWeight, sapNaoh);
				naohForLiquids = weightCalc.plus(naohForLiquids, naoh100);
			}
			// TODO: validate percentage = 100%
		}
		totalWeight = weightCalc.plus(totalWeight, totalLiquidWeight);
		totalCost = priceCalc.plus(totalCost, totalLiquidCosts);

		final Percentage fragranceTotalPercentage = soapRecipe.getFragranceTotal();
		Weight totalFragranceWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalFragranceCosts = Price.of(0);
		if (!CollectionUtils.isEmpty(soapRecipe.getFragrances())
				&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
			for (final RecipeEntry<Fragrance> fragranceEntry : soapRecipe.getFragrances()) {
				final Percentage fragrancePercentage = fragranceEntry.getPercentage();
				final Fragrance fragrance = fragranceEntry.getIngredient();
				final Weight fatsTotal = soapRecipe.getFatsTotal();
				final Weight fragranceWeight = weightCalc.calculatePercentage(fatsTotal, fragranceTotalPercentage,
						fragrancePercentage);
				fragranceEntry.setWeight(fragranceWeight);
				totalFragranceWeight = weightCalc.plus(totalFragranceWeight, fragranceWeight);
				if (fragrance.getCost() == null) {
					log.warning("Ignoring price of ingredient " + fragrance);
				} else {
					final Price fragrancePrice = priceCalc.calculatePriceForWeight(fragrance.getCost(),
							fragranceWeight);
					fragranceEntry.setPrice(fragrancePrice);
					totalFragranceCosts = priceCalc.plus(totalFragranceCosts, fragrancePrice);
				}
			}
			// TODO: validate percentage = 100%
		}
		totalWeight = weightCalc.plus(totalWeight, totalFragranceWeight);
		totalCost = priceCalc.plus(totalCost, totalFragranceCosts);

		Price totalLyeCosts = Price.of(0);
		final Percentage naohPercentage = soapRecipe.getNaOH().getPercentage();
		final Percentage kohPercentage;
		if (soapRecipe.getKOH() == null) {
			kohPercentage = Percentage.of(0d);
		} else {
			kohPercentage = soapRecipe.getKOH().getPercentage();
		}
		// TODO: validate koh+naoh=100%
		final Weight naohForFatsAndAcidsAndLiquids = weightCalc.plus(naohForFats, naohForAcids, naohForLiquids);
		final Weight naohTotal = weightCalc.calculatePercentage(naohForFatsAndAcidsAndLiquids, naohPercentage);
		totalWeight = weightCalc.plus(totalWeight, naohTotal);

		if (soapRecipe.getNaOH().getIngredient().getCost() == null) {
			log.warning("Ignoring price of ingredient " + soapRecipe.getNaOH().getIngredient());
		} else {
			totalLyeCosts = priceCalc.plus(totalLyeCosts, soapRecipe.getNaOH().getIngredient().getCost());
		}

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = soapRecipe.getKOH().getIngredient().getKOHPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			kohTotal = weightCalc.multiply(naohForFatsAndAcidsAndLiquids, naohToKohConversion,
					percentageCalc.divide(kohPercentage, kohPurity));
			totalWeight = weightCalc.plus(totalWeight, kohTotal);

			if (soapRecipe.getKOH().getIngredient().getCost() == null) {
				log.warning("Ignoring price of ingredient " + soapRecipe.getKOH().getIngredient());
			} else {
				totalLyeCosts = priceCalc.plus(totalLyeCosts, soapRecipe.getKOH().getIngredient().getCost());
			}
			totalCost = priceCalc.plus(totalCost, totalLyeCosts);
		}

		soapRecipe.setNaohTotal(naohTotal);
		soapRecipe.setKohTotal(kohTotal);
		soapRecipe.setLiquidTotal(totalLiquidWeight);
		soapRecipe.setWeightTotal(totalWeight);
		soapRecipe.setCostsTotal(totalCost);

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

		validateAndHandleError(() -> soapRecipe.getFatsTotal() == null, soapRecipe,
				issueCollector, CalculationIssue.FATS_TOTAL_MISSING);

		validateAndHandleError(() -> soapRecipe.getFats() == null, soapRecipe, issueCollector,
				CalculationIssue.NO_FAT_IN_RECIPE);

		if (soapRecipe.getNaOH() == null) {

		}
	}

	private void validateAndHandleError(BooleanSupplier validationFailedCheck,
			SoapRecipe soapRecipe, SoapCalculatorIssueCollector issueCollector, CalculationIssue issue) {
		if (validationFailedCheck.getAsBoolean()) {
			issueCollector.addError(issue);
			throw new SoapCalculatorException(env, soapRecipe, issueCollector);
		}
	}
}
