package org.soaplab.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldNameConstants
public class SoapRecipe extends Recipe {

	private static final long serialVersionUID = 1L;

	/**
	 * Liquid in regards to the total amount of fats
	 */
	@Default
	private Percentage liquidToFatRatio = Percentage.of(0);
	/**
	 * Weight of fats in total
	 */
	@Default
	private Weight fatsWeight = Weight.ofGrams(0);
	/**
	 * Amount of superfat in percentage
	 */
	@Default
	private Percentage superFat = Percentage.of(0);;
	/**
	 * Amount of fragrance in percentage regarding total fats
	 */
	@Default
	private Percentage fragranceToFatRatio = Percentage.of(0);

	@ToString.Exclude
	private LyeRecipe lyeRecipe;

	@ToString.Exclude
	private FragranceRecipe fragranceRecipe;

	@ToString.Exclude
	private List<RecipeEntry<Fat>> fats = new ArrayList<>();
	/**
	 * Calculated value. Fats costs.
	 */
	@Default
	private Price fatsCosts = Price.of(0);

	@ToString.Exclude
	private List<RecipeEntry<Additive>> additives = new ArrayList<>();
	/**
	 * Calculated value. Total weight of soap batter additives.
	 */
	@Default
	private Weight additivesWeight = Weight.ofGrams(0);
	/**
	 * Calculated value. Soap batter additives costs.
	 */
	@Default
	private Price additivesCosts = Price.of(0);

	@Override
	public SoapRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.fats(getRecipeEntryListDeepClone(fats)) //
				.additives(getRecipeEntryListDeepClone(additives)) //
				.lyeRecipe(lyeRecipe == null ? null : lyeRecipe.getCopyBuilder().build()) //
				.fragranceRecipe(fragranceRecipe == null ? null : fragranceRecipe.getCopyBuilder().build());
	}

	public void addFat(Fat fat, Double percentage) {
		setFats(createNewRecipeEntryListWithAddedIngredient(fat, percentage, getFats()));
	}

	public void removeFat(Fat fat) {
		setFats(createNewRecipeEntryListWithRemovedIngredient(fat, getFats()));
	}

	public Optional<Fat> getFat(UUID uuid) {
		return getIngredient(uuid, getFats());
	}
}
