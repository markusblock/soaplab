package org.soaplab.domain;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.RecipeEntryAssert;
import org.soaplab.testdata.IngredientsRandomTestData;
import org.soaplab.testdata.RecipeTestDataBuilder;

class RecipeEntryTest {

	@Test
	void testDeepCloning() {
		Fat fat = IngredientsRandomTestData.getFatBuilder().build();
		RecipeEntry<Fat> recipeEntry = RecipeTestDataBuilder.createRecipeEntry(fat, 100d);
		RecipeEntry<Fat> clone = recipeEntry.getClone();

		RecipeEntryAssert.assertThat(recipeEntry).isDeepEqualTo(clone);

	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
