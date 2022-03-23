
package org.soaplab.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
	private String notes;
	private Map<UUID, RecipeEntry<Fat>> fats = new HashMap<>();
	private Map<UUID, RecipeEntry<Acid>> acids = new HashMap<>();
	private Map<UUID, RecipeEntry<Fragrance>> fragrances = new HashMap<>();
	private Map<UUID, RecipeEntry<Liquid>> liquids = new HashMap<>();
	// customAdditives

	@Override
	public SoapRecipe getClone() {
		return new SoapRecipe(this.toBuilder().fats(getRecipeEntryMapDeepClone(fats))
				.acids(getRecipeEntryMapDeepClone(acids)).fragrances(getRecipeEntryMapDeepClone(fragrances))
				.liquids(getRecipeEntryMapDeepClone(liquids)));
	}

	private <T extends Ingredient> Map<UUID, RecipeEntry<T>> getRecipeEntryMapDeepClone(
			Map<UUID, RecipeEntry<T>> recipeEntryMap) {
		return recipeEntryMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getClone()));
	}
}
