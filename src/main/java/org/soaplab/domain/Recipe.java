package org.soaplab.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
public class Recipe extends NamedEntity {

	private static final long serialVersionUID = 1L;

	private Date manufacturingDate;

	/**
	 * Optional notes on the recipt.
	 */
	private String notes;

	/**
	 * Calculated value. Total weight of all ingredients in the recipe.
	 */
	@Default
	private Weight weight = Weight.ofGrams(0);
	/**
	 * Calculated value. Total costs of all ingredients in the recipe.
	 */
	@Default
	private Price costs = Price.of(0);
	/**
	 * Calculated value. Total costs of all ingredients in the recipe per 100g.
	 */
	@Default
	private Price costsPer100g = Price.of(0);

	public static <T extends Ingredient> RecipeEntry<T> createRecipeEntry(T ingredient, Double percentage) {
		return RecipeEntry.<T>builder().ingredient(ingredient).percentage(Percentage.of(percentage)).build();
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> createRecipeEntries(RecipeEntry<T>... recipeEntries) {
		return List.of(recipeEntries);
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> getRecipeEntryListDeepClone(
			List<RecipeEntry<T>> recipeEntries) {
		return recipeEntries.stream().map(entry -> entry.getCopyBuilder().build()).collect(Collectors.toList());
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> createNewRecipeEntryListWithAddedIngredient(T ingredient,
			Double percentage, List<RecipeEntry<T>> listWithRecipientEntries) {
		final List<RecipeEntry<T>> newRecipeEntries = new ArrayList<>(listWithRecipientEntries);
		newRecipeEntries.add(createRecipeEntry(ingredient, percentage));
		return newRecipeEntries;
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> createNewRecipeEntryListWithRemovedIngredient(
			T ingredient, List<RecipeEntry<T>> listWithRecipientEntries) {
		final List<RecipeEntry<T>> newRecipeEntries = listWithRecipientEntries.stream()
				.filter(entry -> !entry.getIngredient().getId().equals(ingredient.getId())).toList();
		return newRecipeEntries;
	}

	public static <T extends Ingredient> Optional<T> getIngredient(UUID uuid,
			List<RecipeEntry<T>> listWithRecipientEntries) {
		return listWithRecipientEntries.stream().map(recipeEntry -> recipeEntry.getIngredient())
				.filter(ingredient -> ingredient.getId().equals(uuid)).findFirst();
	}

	public static <T extends Ingredient> RecipeEntry<T> getRecipeEntry(T ingredient,
			List<RecipeEntry<T>> listWithRecipientEntries) {
		return listWithRecipientEntries.stream().filter(recipeEntry -> recipeEntry.getIngredient().equals(ingredient))
				.findAny().orElseThrow();
	}
}
