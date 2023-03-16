package org.soaplab.assertions;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;

public class SoapRecipeAssert extends AbstractAssert<SoapRecipeAssert, SoapRecipe> {

	public SoapRecipeAssert(SoapRecipe actual) {
		super(actual, SoapRecipeAssert.class);
	}

	public static SoapRecipeAssert assertThat(SoapRecipe actual) {
		return new SoapRecipeAssert(actual);
	}

	public SoapRecipeAssert isDeepEqualTo(SoapRecipe expected) {

		isNotNull();

		Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
		Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
		Assertions.assertThat(actual.getFatsTotal()).isEqualTo(expected.getFatsTotal());
		Assertions.assertThat(actual.getFragranceToFatRatio()).isEqualTo(expected.getFragranceToFatRatio());
		Assertions.assertThat(actual.getLiquidToFatRatio()).isEqualTo(expected.getLiquidToFatRatio());
		Assertions.assertThat(actual.getManufacturingDate()).isEqualTo(expected.getManufacturingDate());
		Assertions.assertThat(actual.getNotes()).isEqualTo(expected.getNotes());
		Assertions.assertThat(actual.getSuperFat()).isEqualTo(expected.getSuperFat());

		RecipeEntryAssert.assertThat(actual.getKOH()).isDeepEqualTo(expected.getKOH());
		RecipeEntryAssert.assertThat(actual.getNaOH()).isDeepEqualTo(expected.getNaOH());

		assertIngredientMapsAreDeepEqual(actual.getAcids(), expected.getAcids());
		assertIngredientMapsAreDeepEqual(actual.getFats(), expected.getFats());
		assertIngredientMapsAreDeepEqual(actual.getFragrances(), expected.getFragrances());
		assertIngredientMapsAreDeepEqual(actual.getLiquids(), expected.getLiquids());

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
