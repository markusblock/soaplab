package org.soaplab.domain;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.SoapRecipeAssert;
import org.soaplab.testdata.OliveOilSoapRecipeTestData;

class SoapRecipeTest {

	@Test
	void testDeepCloning() {
		final OliveOilSoapRecipeTestData testData = new OliveOilSoapRecipeTestData();
		final SoapRecipe soapRecipe = testData.createSoapRecipe();
		final SoapRecipe clone = soapRecipe.getCopyBuilder().build();

		SoapRecipeAssert.assertThat(soapRecipe).isDeepEqualTo(clone);

	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
