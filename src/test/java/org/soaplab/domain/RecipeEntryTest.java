package org.soaplab.domain;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.RecipeEntryAssert;
import org.soaplab.domain.utils.SoapRecipeUtils;
import org.soaplab.testdata.RandomIngredientsTestData;

class RecipeEntryTest {

	@Test
	void testDeepCloning() {
		Fat fat = RandomIngredientsTestData.getFatBuilder().build();
		RecipeEntry<Fat> recipeEntry = SoapRecipeUtils.createRecipeEntry(fat, 100d);
		RecipeEntry<Fat> clone = recipeEntry.getClone();

		RecipeEntryAssert.assertThat(recipeEntry).isDeepEqualTo(clone);

	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
