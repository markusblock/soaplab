package org.soaplab.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.RecipeEntry;

public class RecipeEntryAssert<T extends Ingredient> extends AbstractAssert<RecipeEntryAssert<T>, RecipeEntry<T>> {

	public RecipeEntryAssert(RecipeEntry<T> actual) {
		super(actual, RecipeEntryAssert.class);
	}

	public static <T extends Ingredient> RecipeEntryAssert<T> assertThat(RecipeEntry<T> actual) {
		return new RecipeEntryAssert<T>(actual);
	}

	public RecipeEntryAssert<T> isDeepEqualTo(RecipeEntry<T> expected) {

		isNotNull();

		Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
		PercentageAssert.assertThat(actual.getPercentage()).isDeepEqualTo(expected.getPercentage());
		IngredientAssert.assertThat(actual.getIngredient()).isDeepEqualTo(expected.getIngredient());

		return this;
	}
}
