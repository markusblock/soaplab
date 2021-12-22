package org.soaplab.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.soaplab.domain.Acid;
import org.soaplab.domain.CalculatedRecipeEntry;
import org.soaplab.domain.CalculatedSoapRecipeResult;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;
import org.springframework.stereotype.Component;

@Component
public class SoapCalculatorService {

	public SoapCalculatorService() {
	}

	public CalculatedSoapRecipeResult calculate(SoapRecipe soapReceipt) {

		Weight totalWeight = Weight.of(0, WeightUnit.GRAMS);

		final Map<UUID, CalculatedRecipeEntry<Fat>> fats = new HashMap<>();
		final Map<UUID, CalculatedRecipeEntry<Acid>> acids = new HashMap<>();
		final Map<UUID, CalculatedRecipeEntry<Fragrance>> fragrances = new HashMap<>();
		final Map<UUID, CalculatedRecipeEntry<Liquid>> liquids = new HashMap<>();

		Weight naohForFats = Weight.of(0, WeightUnit.GRAMS);
		for (RecipeEntry<Fat> fatentry : soapReceipt.getFats().values()) {
			Percentage fatPercentage = fatentry.getPercentage();
			Fat fat = fatentry.getIngredient();
			Double sapNaoh = fat.getSapNaoh();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Weight fatWeight = fatsTotal.calculatePercentage(fatPercentage);
			Weight naoh100 = fatWeight.multiply(sapNaoh);
			Percentage superFat = soapReceipt.getSuperFat();
			Weight naohReduction = naoh100.calculatePercentage(superFat);
			Weight naohPerFat = naoh100.subtract(naohReduction);

			naohForFats = naohForFats.plus(naohPerFat);
			addToIngredientMap(fats, fatentry, fatWeight);
			totalWeight = totalWeight.plus(fatWeight);
		}

		Weight naohForAcids = Weight.of(0, WeightUnit.GRAMS);
		for (RecipeEntry<Acid> acidEntry : soapReceipt.getAcids().values()) {
			Percentage acidPercentage = acidEntry.getPercentage();
			Acid acid = acidEntry.getIngredient();
			Double sapNaoh = acid.getSapNaoh();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Weight acidWeight = fatsTotal.calculatePercentage(acidPercentage);
			Weight naoh100 = acidWeight.multiply(sapNaoh);

			naohForAcids = naohForAcids.plus(naoh100);
			addToIngredientMap(acids, acidEntry, acidWeight);
			totalWeight = totalWeight.plus(acidWeight);
		}

		Weight naohForLiquids = Weight.of(0, WeightUnit.GRAMS);
		Weight liquidTotal = Weight.of(0, WeightUnit.GRAMS);
		for (RecipeEntry<Liquid> liquidEntry : soapReceipt.getLiquids().values()) {
			Percentage liquidPercentage = liquidEntry.getPercentage();
			Liquid liquid = liquidEntry.getIngredient();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Percentage liquidToFatRatio = soapReceipt.getLiquidToFatRatio();
			Weight liquidWeight = fatsTotal.calculatePercentage(liquidToFatRatio).calculatePercentage(liquidPercentage);
			addToIngredientMap(liquids, liquidEntry, liquidWeight);
			liquidTotal = liquidTotal.plus(liquidWeight);
			totalWeight = totalWeight.plus(liquidWeight);

			Double sapNaoh = liquid.getSapNaoh();
			if (sapNaoh != null) {
				Weight naoh100 = liquidWeight.multiply(sapNaoh);
				naohForLiquids = naohForLiquids.plus(naoh100);
			}
		}

		for (RecipeEntry<Fragrance> fragranceEntry : soapReceipt.getFragrances().values()) {
			Percentage fragrancePercentage = fragranceEntry.getPercentage();
			Weight fatsTotal = soapReceipt.getFatsTotal();
			Percentage fragranceTotalPercentage = soapReceipt.getFragranceTotal();
			Weight fragranceWeight = fatsTotal.calculatePercentage(fragranceTotalPercentage)
					.calculatePercentage(fragrancePercentage);
			addToIngredientMap(fragrances, fragranceEntry, fragranceWeight);
			totalWeight = totalWeight.plus(fragranceWeight);
		}

		Percentage naohPercentage = soapReceipt.getNaOHToKOHRatio();
		Weight naohForFatsAndAcidsAndLiquids = naohForFats.plus(naohForAcids).plus(naohForLiquids);
		Weight naohTotal = naohForFatsAndAcidsAndLiquids.calculatePercentage(naohPercentage);
		Percentage kohPercentage = Percentage.of(100).minus(soapReceipt.getNaOHToKOHRatio());

		Weight kohTotal = Weight.of(0, WeightUnit.GRAMS);
		if (kohPercentage.getNumber() > 0) {
			Percentage kohPurity = soapReceipt.getKOHPurity();
			Double naohToKohConversion = Double.valueOf(1.40272);
			kohTotal = naohForFatsAndAcidsAndLiquids.multiply(naohToKohConversion)
					.multiply(kohPercentage.getNumber() / kohPurity.getNumber());
		}

		return CalculatedSoapRecipeResult.builder().naohTotal(naohTotal).kohTotal(kohTotal).liquidTotal(liquidTotal)
				.fats(fats).acids(acids).fragrances(fragrances).liquids(liquids).build();
	}

	private static <T extends Ingredient> void addToIngredientMap(
			Map<UUID, CalculatedRecipeEntry<T>> calculatedIngredientsMap, RecipeEntry<T> recipeEntry, Weight weight) {
		CalculatedRecipeEntry<T> calculatedRecipeEntry = CalculatedRecipeEntry.<T>builder()
				.ingredient(recipeEntry.getIngredient()).weight(weight).build();
		calculatedIngredientsMap.put(recipeEntry.getIngredient().getId(), calculatedRecipeEntry);
	}
}
