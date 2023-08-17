
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
	 * Optional notes on the recipt.
	 */
	private String notes;
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
	 * Amount of fragrance in percentage regarding total fats
	 */
	private Percentage fragranceToFatRatio;

	private LyeRecipe lyeRecipe;

	private List<RecipeEntry<Fragrance>> fragrances = new ArrayList<>();
	/**
	 * Calculated value. Total weight of fragrances.
	 */
	private Weight fragrancesTotal;
	/**
	 * Calculated value. Fragrances costs.
	 */
	private Price fragrancesCosts;

	private List<RecipeEntry<Fat>> fats = new ArrayList<>();
	/**
	 * Calculated value. Fats costs.
	 */
	private Price fatsCosts;

	private List<RecipeEntry<Additive>> additives = new ArrayList<>();
	/**
	 * Calculated value. Total weight of soap batter additives.
	 */
	private Weight additivesTotal;
	/**
	 * Calculated value. Soap batter additives costs.
	 */
	private Price additivesCosts;

	/**
	 * Calculated value. Total weight of all ingredients in the recipe.
	 */
	private Weight weightTotal;
	/**
	 * Calculated value. Total costs of all ingredients in the recipe.
	 */
	private Price costsTotal;
	/**
	 * Calculated value. Total costs of all ingredients in the recipe per 100g.
	 */
	private Price costsTotalPer100g;

	@Override
	public SoapRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.fats(getRecipeEntryListDeepClone(fats)) //
				.fragrances(getRecipeEntryListDeepClone(fragrances)) //
				.additives(getRecipeEntryListDeepClone(additives));
	}

	private <T extends Ingredient> List<RecipeEntry<T>> getRecipeEntryListDeepClone(
			List<RecipeEntry<T>> recipeEntries) {
		return recipeEntries.stream().map(entry -> entry.getCopyBuilder().build()).collect(Collectors.toList());
	}
}
