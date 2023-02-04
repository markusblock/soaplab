package org.soaplab.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import org.soaplab.domain.utils.WeightCalculator;
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
public class SoapCalculatorService {

	private static RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static int decimalPlaces = 4;
	private final WeightCalculator weightCalc = new WeightCalculator(decimalPlaces, roundingMode);
	private final PercentageCalculator percentageCalc = new PercentageCalculator(decimalPlaces, roundingMode);

	public SoapCalculatorService() {
	}

	public SoapRecipe calculate(final SoapRecipe soapRecipe) {

		Weight totalWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalCost = Price.of(0);

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
			final Price fatPrice = fat.getCost();
			fatentry.setPrice(fatPrice);
			final Weight naoh100 = weightCalc.multiply(fatWeight, sapNaoh);
			final Percentage superFat = soapRecipe.getSuperFat();
			final Weight naohReduction = weightCalc.calculatePercentage(naoh100, superFat);
			final Weight naohPerFat = weightCalc.subtract(naoh100, naohReduction);

			naohForFats = weightCalc.plus(naohForFats, naohPerFat);
			totalFatWeight = weightCalc.plus(totalFatWeight, fatWeight);

			if (fatPrice == null) {
				log.warning("Ignoring price of ingredient " + fat);
			} else {
				totalFatCosts = totalFatCosts.plus(fatPrice);
			}
		}
		totalWeight = weightCalc.plus(totalWeight, totalFatWeight);
		totalCost = totalCost.plus(totalFatCosts);

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
				final Price acidPrice = acid.getCost();
				acidEntry.setPrice(acidPrice);
				final Weight naoh100 = weightCalc.multiply(acidWeight, sapNaoh);

				naohForAcids = weightCalc.plus(naohForAcids, naoh100);
				totalAcidWeight = weightCalc.plus(totalFatWeight, acidWeight);

				if (acidPrice == null) {
					log.warning("Ignoring price of ingredient " + acid);
				} else {
					totalAcidCosts = totalAcidCosts.plus(acidPrice);
				}
			}
		}
		totalWeight = weightCalc.plus(totalWeight, totalAcidWeight);
		totalCost = totalCost.plus(totalAcidCosts);

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
			final Price liquidPrice = liquid.getCost();
			liquidEntry.setPrice(liquidPrice);
			totalLiquidWeight = weightCalc.plus(totalLiquidWeight, liquidWeight);

			if (liquidPrice == null) {
				log.warning("Ignoring price of ingredient " + liquid);
			} else {
				totalLiquidCosts = totalLiquidCosts.plus(liquidPrice);
			}

			final BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				final Weight naoh100 = weightCalc.multiply(liquidWeight, sapNaoh);
				naohForLiquids = weightCalc.plus(naohForLiquids, naoh100);
			}
		}
		totalWeight = weightCalc.plus(totalWeight, totalLiquidWeight);
		totalCost = totalCost.plus(totalLiquidCosts);

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
				// TODO: adapt cost calculation quantity * costs
				final Price fragrancePrice = fragrance.getCost();
				fragranceEntry.setPrice(fragrancePrice);
				totalFragranceWeight = weightCalc.plus(totalFragranceWeight, fragranceWeight);
				if (fragrancePrice == null) {
					log.warning("Ignoring price of ingredient " + fragrance);
				} else {
					totalFragranceCosts = totalFragranceCosts.plus(fragrancePrice);
				}
			}
		}
		totalWeight = weightCalc.plus(totalWeight, totalFragranceWeight);
		totalCost = totalCost.plus(totalFragranceCosts);

		final Percentage naohPercentage = soapRecipe.getNaOH().getPercentage();
		final Percentage kohPercentage;
		if (soapRecipe.getKOH() == null) {
			kohPercentage = Percentage.of(0d);
		} else {
			kohPercentage = soapRecipe.getKOH().getPercentage();
		}
		// validate koh+naoh=100%
		final Weight naohForFatsAndAcidsAndLiquids = weightCalc.plus(naohForFats, naohForAcids, naohForLiquids);
		final Weight naohTotal = weightCalc.calculatePercentage(naohForFatsAndAcidsAndLiquids, naohPercentage);
		totalWeight = weightCalc.plus(totalWeight, naohTotal);

		if (soapRecipe.getNaOH().getIngredient().getCost() == null) {
			log.warning("Ignoring price of ingredient " + soapRecipe.getNaOH().getIngredient());
		} else {
			totalFragranceCosts = totalFragranceCosts.plus(soapRecipe.getNaOH().getIngredient().getCost());
		}

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = soapRecipe.getKOH().getIngredient().getKOHPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			kohTotal = weightCalc.multiply(naohForFatsAndAcidsAndLiquids, naohToKohConversion,
					percentageCalc.divide(kohPercentage, kohPurity));
			totalWeight = weightCalc.plus(totalWeight, kohTotal);
			// TODO cost per 100g => 1/100* weight

			if (soapRecipe.getKOH().getIngredient().getCost() == null) {
				log.warning("Ignoring price of ingredient " + soapRecipe.getKOH().getIngredient());
			} else {
				totalCost = totalCost.plus(soapRecipe.getKOH().getIngredient().getCost());
			}
		}

		soapRecipe.setNaohTotal(naohTotal);
		soapRecipe.setKohTotal(kohTotal);
		soapRecipe.setLiquidTotal(totalLiquidWeight);
		soapRecipe.setWeightTotal(totalWeight);
		soapRecipe.setCostsTotal(totalCost);

		return soapRecipe;
	}
}
