
package org.soaplab.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SoapRecipe extends NamedEntity {

	private static final long serialVersionUID = 1L;

	private Date manufacturingDate;
	/**
	 * NaOH to KOH ratio in Percentage. 80% means 80% NaOH and 20% KOH.
	 */
	private Percentage naOHToKOHRatio;
	/**
	 * The purity of the KOH
	 */
	private Percentage kOHPurity;
	/**
	 * Liquid in regards to the total amount of fats
	 */
	private Percentage liquidToFatRatio;
	/**
	 * Weight of fats in total
	 */
	private Weight fatsTotal;
	/**
	 * Amount of superfat in percentage
	 */
	private Percentage superFat;
	/**
	 * Amount of fragrance in percentage regarding total oil
	 */
	private Percentage fragranceTotal;

	/**
	 * Optional notes on the recipt.
	 */
	private String notes;
	/**
	 * Calculated value. Total amount of NaOH.
	 */
	private Weight naohTotal;
	/**
	 * Calculated value. Total amount of KOH.
	 */
	private Weight kohTotal;
	/**
	 * Calculated value. Total amount of liquids.
	 */
	private Weight liquidTotal;
	/**
	 * Calculated value. Total weight of all ingredients in the recipe.
	 */
	private Weight weightTotal;
	/**
	 * Calculated value. Total costs of all ingredients in the recipe.
	 */
	private Price costsTotal;

	private List<RecipeEntry<Fat>> fats = new ArrayList<>();
	private List<RecipeEntry<Acid>> acids = new ArrayList<>();
	private List<RecipeEntry<Fragrance>> fragrances = new ArrayList<>();
	private List<RecipeEntry<Liquid>> liquids = new ArrayList<>();
	// customAdditives

	@Override
	public SoapRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder().fats(getRecipeEntryListDeepClone(fats)).acids(getRecipeEntryListDeepClone(acids))
				.fragrances(getRecipeEntryListDeepClone(fragrances)).liquids(getRecipeEntryListDeepClone(liquids));
	}

	private <T extends Ingredient> List<RecipeEntry<T>> getRecipeEntryListDeepClone(
			List<RecipeEntry<T>> recipeEntries) {
		return recipeEntries.stream().map(entry -> entry.getCopyBuilder().build()).collect(Collectors.toList());
	}
}
