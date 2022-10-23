package org.soaplab.domain;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.SoapRecipeAssert;
import org.soaplab.testdata.OliveOilSoapRecipeTestData;

class SoapRecipeTest {

	@Test
	void testDeepCloning() {
		OliveOilSoapRecipeTestData testData = new OliveOilSoapRecipeTestData();
		SoapRecipe soapRecipe = testData.createSoapRecipe();
		SoapRecipe clone = soapRecipe.getClone();

		SoapRecipeAssert.assertThat(soapRecipe).isDeepEqualTo(clone);

	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
