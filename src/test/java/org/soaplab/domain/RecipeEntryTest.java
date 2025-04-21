package org.soaplab.domain;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.RecipeEntryAssert;
import org.soaplab.testdata.RandomIngredientsTestData;

class RecipeEntryTest {

	@Test
	void testDeepCloning() {
		final Fat fat = RandomIngredientsTestData.getFatBuilder().build();
		final RecipeEntry<Fat> recipeEntry = Recipe.createRecipeEntry(fat, 100d);
		final RecipeEntry<Fat> clone = recipeEntry.getCopyBuilder().build();

		RecipeEntryAssert.assertThat(recipeEntry).isDeepEqualTo(clone);

	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
