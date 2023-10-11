package org.soaplab.domain.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.soaplab.domain.Fat;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;

/**
 * Utilities dealing with SoapRecipe
 *
 */
public class SoapRecipeUtils {

	public static SoapRecipe addFat(SoapRecipe soapRecipe, Fat fat, Double percentage) {
		final List<RecipeEntry<Fat>> recipeEntries = new ArrayList<>(soapRecipe.getFats());
		recipeEntries.add(SoapRecipeUtils.createRecipeEntry(fat, percentage));
		return soapRecipe.getCopyBuilder().fats(recipeEntries).build();
	}

	public static SoapRecipe removeFat(SoapRecipe soapRecipe, Fat fat) {
		final List<RecipeEntry<Fat>> newListOfFats = new ArrayList<>(soapRecipe.getFats());
		final Optional<RecipeEntry<Fat>> optional = newListOfFats.stream()
				.filter(entry -> entry.getIngredient().getId().equals(fat.getId())).findFirst();
		if (optional.isPresent()) {
			final RecipeEntry<Fat> fatEntry = optional.get();
			newListOfFats.remove(fatEntry);
			return soapRecipe.getCopyBuilder().fats(newListOfFats).build();
		}
		return soapRecipe;
	}

	public static Optional<Fat> getFat(SoapRecipe soapRecipe, UUID uuid) {
		final Optional<RecipeEntry<Fat>> recipeEntry = soapRecipe.getFats().stream()
				.filter(entry -> entry.getIngredient().getId().equals(uuid)).findFirst();
		if (recipeEntry.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(recipeEntry.get().getIngredient());

	}

	public static <T extends Ingredient> RecipeEntry<T> createRecipeEntry(T ingredient, Double percentage) {
		return RecipeEntry.<T>builder().id(ingredient.getId()).ingredient(ingredient)
				.percentage(Percentage.of(percentage)).build();
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> createRecipeEntries(RecipeEntry<T>... recipeEntries) {
		return List.of(recipeEntries);
	}

	public static <T extends Ingredient> List<RecipeEntry<T>> getRecipeEntryListDeepClone(
			List<RecipeEntry<T>> recipeEntries) {
		return recipeEntries.stream().map(entry -> entry.getCopyBuilder().build()).collect(Collectors.toList());
	}
}
