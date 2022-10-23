package org.soaplab.service;

import java.math.BigDecimal;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Percentage;
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

	public SoapRecipe calculate(SoapRecipe soapRecipe) {

		Weight totalWeight = Weight.of(0, WeightUnit.GRAMS);
		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		for (RecipeEntry<Fat> fatentry : soapRecipe.getFats()) {
			Percentage fatPercentage = fatentry.getPercentage();
			Fat fat = fatentry.getIngredient();
			BigDecimal sapNaoh = fat.getSapNaoh();
			Weight fatsTotal = soapRecipe.getFatsTotal();
			Weight fatWeight = fatsTotal.calculatePercentage(fatPercentage);
			fatentry.setWeight(fatWeight);
			Weight naoh100 = fatWeight.multiply(sapNaoh);
			Percentage superFat = soapRecipe.getSuperFat();
			Weight naohReduction = naoh100.calculatePercentage(superFat);
			Weight naohPerFat = naoh100.subtract(naohReduction);

			naohForFats = naohForFats.plus(naohPerFat);
			totalWeight = totalWeight.plus(fatWeight);
		}

		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		if (!CollectionUtils.isEmpty(soapRecipe.getAcids())) {
			for (RecipeEntry<Acid> acidEntry : soapRecipe.getAcids()) {
				Percentage acidPercentage = acidEntry.getPercentage();
				Acid acid = acidEntry.getIngredient();
				BigDecimal sapNaoh = acid.getSapNaoh();
				Weight fatsTotal = soapRecipe.getFatsTotal();
				Weight acidWeight = fatsTotal.calculatePercentage(acidPercentage);
				acidEntry.setWeight(acidWeight);
				Weight naoh100 = acidWeight.multiply(sapNaoh);

				naohForAcids = naohForAcids.plus(naoh100);
				totalWeight = totalWeight.plus(acidWeight);
			}
		}

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		Weight liquidTotal = Weight.of(0, WeightUnit.GRAMS);
		for (RecipeEntry<Liquid> liquidEntry : soapRecipe.getLiquids()) {
			Percentage liquidPercentage = liquidEntry.getPercentage();
			Liquid liquid = liquidEntry.getIngredient();
			Weight fatsTotal = soapRecipe.getFatsTotal();
			Percentage liquidToFatRatio = soapRecipe.getLiquidToFatRatio();
			Weight liquidWeight = fatsTotal.calculatePercentage(liquidToFatRatio).calculatePercentage(liquidPercentage);
			liquidEntry.setWeight(liquidWeight);
			liquidTotal = liquidTotal.plus(liquidWeight);
			totalWeight = totalWeight.plus(liquidWeight);

			BigDecimal sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				Weight naoh100 = liquidWeight.multiply(sapNaoh);
				naohForLiquids = naohForLiquids.plus(naoh100);
			}
		}

		Percentage fragranceTotalPercentage = soapRecipe.getFragranceTotal();
		if (!CollectionUtils.isEmpty(soapRecipe.getFragrances())
				&& Percentage.isGreaterThanZero(fragranceTotalPercentage)) {
			for (RecipeEntry<Fragrance> fragranceEntry : soapRecipe.getFragrances()) {
				Percentage fragrancePercentage = fragranceEntry.getPercentage();
				Weight fatsTotal = soapRecipe.getFatsTotal();
				Weight fragranceWeight = fatsTotal.calculatePercentage(fragranceTotalPercentage)
						.calculatePercentage(fragrancePercentage);
				fragranceEntry.setWeight(fragranceWeight);
				totalWeight = totalWeight.plus(fragranceWeight);
			}
		}

		Percentage naohPercentage = soapRecipe.getNaOHToKOHRatio();
		Weight naohForFatsAndAcidsAndLiquids = naohForFats.plus(naohForAcids).plus(naohForLiquids);
		Weight naohTotal = naohForFatsAndAcidsAndLiquids.calculatePercentage(naohPercentage);
		Percentage kohPercentage = Percentage.of(100).minus(soapRecipe.getNaOHToKOHRatio());

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (Percentage.isGreaterThanZero(kohPercentage)) {
			Percentage kohPurity = soapRecipe.getKOHPurity();
			BigDecimal naohToKohConversion = BigDecimal.valueOf(1.40272);
			kohTotal = naohForFatsAndAcidsAndLiquids.multiply(naohToKohConversion)
					.multiply(kohPercentage.getNumber().divide(kohPurity.getNumber()));
		}

		soapRecipe.setNaohTotal(naohTotal);
		soapRecipe.setKohTotal(kohTotal);
		soapRecipe.setLiquidTotal(liquidTotal);
		soapRecipe.setWeightTotal(totalWeight);

		return soapRecipe;
	}
}
