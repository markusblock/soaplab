package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.FatAssert;
import org.soaplab.testdata.IngredientsTestData;

class FatTest {

	@Test
	void testEntityWithSameIdIsEqual() {
		UUID uuid = UUID.randomUUID();
		Fat fat1 = Fat.builder().id(uuid).name("test").build();
		Fat fat2 = Fat.builder().id(uuid).name("test").build();
		assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdIsButDifferentValuesEqual() {
		UUID uuid = UUID.randomUUID();
		Fat fat1 = Fat.builder().id(uuid).name("test").build();
		Fat fat2 = Fat.builder().id(uuid).name("xxx").build();
		assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithDifferentIdIsNotEqual() {
		Fat fat1 = Fat.builder().id(UUID.randomUUID()).name("test").build();
		Fat fat2 = Fat.builder().id(UUID.randomUUID()).name("test").build();
		assertThat(fat1).isNotEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdButDifferentTypeNotEqual() {
		UUID uuid = UUID.randomUUID();
		Fat fat1 = Fat.builder().id(uuid).name("test").build();
		Acid acid = Acid.builder().id(uuid).name("test").build();
		assertThat(fat1).isNotEqualTo(acid);
	}

	@Test
	void testDeepCloning() {
		Fat originalFat = IngredientsTestData.getCoconutOilBuilder().build();
		Fat clone = originalFat.getClone();
		FatAssert.assertThat(originalFat).isDeepEqualTo(clone);
	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
