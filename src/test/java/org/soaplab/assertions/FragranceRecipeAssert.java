package org.soaplab.assertions;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.soaplab.domain.FragranceRecipe;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.RecipeEntry;

public class FragranceRecipeAssert extends AbstractAssert<FragranceRecipeAssert, FragranceRecipe> {

	public FragranceRecipeAssert(FragranceRecipe actual) {
		super(actual, FragranceRecipeAssert.class);
	}

	public static FragranceRecipeAssert assertThat(FragranceRecipe actual) {
		return new FragranceRecipeAssert(actual);
	}

	public FragranceRecipeAssert isDeepEqualTo(FragranceRecipe expected) {

		isNotNull();

		Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
		Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
		Assertions.assertThat(actual.getNotes()).isEqualTo(expected.getNotes());

		assertIngredientMapsAreDeepEqual(actual.getFragrances(), expected.getFragrances());

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
