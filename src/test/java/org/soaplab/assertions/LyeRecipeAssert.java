package org.soaplab.assertions;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.RecipeEntry;

public class LyeRecipeAssert extends AbstractAssert<LyeRecipeAssert, LyeRecipe> {

	public LyeRecipeAssert(LyeRecipe actual) {
		super(actual, LyeRecipeAssert.class);
	}

	public static LyeRecipeAssert assertThat(LyeRecipe actual) {
		return new LyeRecipeAssert(actual);
	}

	public LyeRecipeAssert isDeepEqualTo(LyeRecipe expected) {

		isNotNull();

		Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
		Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
		Assertions.assertThat(actual.getNotes()).isEqualTo(expected.getNotes());

		RecipeEntryAssert.assertThat(actual.getKOH()).isDeepEqualTo(expected.getKOH());
		RecipeEntryAssert.assertThat(actual.getNaOH()).isDeepEqualTo(expected.getNaOH());

		assertIngredientMapsAreDeepEqual(actual.getAcids(), expected.getAcids());
		assertIngredientMapsAreDeepEqual(actual.getLiquids(), expected.getLiquids());
		assertIngredientMapsAreDeepEqual(actual.getAdditives(), expected.getAdditives());

		return this;
	}

	private <T extends Ingredient> void assertIngredientMapsAreDeepEqual(List<RecipeEntry<T>> actual,
			List<RecipeEntry<T>> expected) {
		Assertions.assertThat(actual).hasSameSizeAs(expected);
		actual.forEach(entry -> {
			Assertions.assertThat(expected).contains(entry);
			RecipeEntryAssert.assertThat(entry).isDeepEqualTo(expected.get(expected.indexOf(entry)));
		});
	}
}
