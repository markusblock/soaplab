package org.soaplab.service;

import java.math.BigDecimal;

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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SoapCalculatorService {

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
			final Weight fatWeight = fatsTotal.calculatePercentage(fatPercentage);
			fatentry.setWeight(fatWeight);
			final Price fatPrice = fat.getCost();
			fatentry.setPrice(fatPrice);
			final Weight naoh100 = fatWeight.multiply(sapNaoh);
			final Percentage superFat = soapRecipe.getSuperFat();
			final Weight naohReduction = naoh100.calculatePercentage(superFat);
			final Weight naohPerFat = naoh100.subtract(naohReduction);

			naohForFats = naohForFats.plus(naohPerFat);
			totalFatWeight = totalFatWeight.plus(fatWeight);
			totalFatCosts = totalFatCosts.plus(fatPrice);
		}
		totalWeight = totalWeight.plus(totalFatWeight);
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
				final Weight acidWeight = fatsTotal.calculatePercentage(acidPercentage);
				acidEntry.setWeight(acidWeight);
				final Price acidPrice = acid.getCost();
				acidEntry.setPrice(acidPrice);
				final Weight naoh100 = acidWeight.multiply(sapNaoh);

				naohForAcids = naohForAcids.plus(naoh100);
				totalAcidWeight = totalFatWeight.plus(acidWeight);
				totalAcidCosts = totalAcidCosts.plus(acidPrice);
			}
		}
		totalWeight = totalWeight.plus(totalAcidWeight);
		totalCost = totalCost.plus(totalAcidCosts);

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		Weight totalLiquidWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalLiquidCosts = Price.of(0);
		for (final RecipeEntry<Liquid> liquidEntry : soapRecipe.getLiquids()) {
			final Percentage liquidPercentage = liquidEntry.getPercentage();
			final Liquid liquid = liquidEntry.getIngredient();
			final Weight fatsTotal = soapRecipe.getFatsTotal();
			final Percentage liquidToFatRatio = soapRecipe.getLiquidToFatRatio();
			final Weight liquidWeight = fatsTotal.calculatePercentage(liquidToFatRatio)
					.calculatePercentage(liquidPercentage);
			liquidEntry.setWeight(liquidWeight);
			final Price liquidPrice = liquid.getCost();
			liquidEntry.setPrice(liquidPrice);
			totalLiquidWeight = totalLiquidWeight.plus(liquidWeight);
			totalLiquidCosts = totalLiquidCosts.plus(liquidPrice);

			final BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				final Weight naoh100 = liquidWeight.multiply(sapNaoh);
				naohForLiquids = naohForLiquids.plus(naoh100);
			}
		}
		totalWeight = totalWeight.plus(totalLiquidWeight);
		totalCost = totalCost.plus(totalLiquidCosts);

		final Percentage fragranceTotalPercentage = soapRecipe.getFragranceTotal();
		Weight totalFragranceWeight = Weight.of(0, WeightUnit.GRAMS);
		Price totalFragranceCosts = Price.of(0);
		if (!CollectionUtils.isEmpty(soapRecipe.getFragrances())
				&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
			for (final RecipeEntry<Fragrance> fragranceEntry : soapRecipe.getFragrances()) {
				final Percentage fragrancePercentage = fragranceEntry.getPercentage();
				final Weight fatsTotal = soapRecipe.getFatsTotal();
				final Weight fragranceWeight = fatsTotal.calculatePercentage(fragranceTotalPercentage)
						.calculatePercentage(fragrancePercentage);
				fragranceEntry.setWeight(fragranceWeight);
				final Price fragrancePrice = fragranceEntry.getIngredient().getCost();
				fragranceEntry.setPrice(fragrancePrice);
				totalFragranceWeight = totalFragranceWeight.plus(fragranceWeight);
				totalFragranceCosts = totalFragranceCosts.plus(fragrancePrice);
			}
		}
		totalWeight = totalWeight.plus(totalFragranceWeight);
		totalCost = totalCost.plus(totalFragranceCosts);

		final Percentage naohPercentage = soapRecipe.getNaOH().getPercentage();
		final Percentage kohPercentage = soapRecipe.getKOH().getPercentage();
		// validate koh+naoh=100%
		final Weight naohForFatsAndAcidsAndLiquids = naohForFats.plus(naohForAcids).plus(naohForLiquids);
		final Weight naohTotal = naohForFatsAndAcidsAndLiquids.calculatePercentage(naohPercentage);
		totalWeight = totalWeight.plus(naohTotal);
		totalCost = totalCost.plus(soapRecipe.getNaOH().getIngredient().getCost());

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			final Percentage kohPurity = soapRecipe.getKOH().getIngredient().getKOHPurity();
			final BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			kohTotal = naohForFatsAndAcidsAndLiquids.multiply(naohToKohConversion)
					.multiply(kohPercentage.getNumber().divide(kohPurity.getNumber()));
			totalWeight = totalWeight.plus(kohTotal);
			// TODO cost per 100g => 1/100* weight
			totalCost = totalCost.plus(soapRecipe.getKOH().getIngredient().getCost());
		}

		soapRecipe.setNaohTotal(naohTotal);
		soapRecipe.setKohTotal(kohTotal);
		soapRecipe.setLiquidTotal(totalLiquidWeight);
		soapRecipe.setWeightTotal(totalWeight);

		return soapRecipe;
	}
}
