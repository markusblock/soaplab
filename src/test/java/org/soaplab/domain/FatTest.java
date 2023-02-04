package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.FatAssert;
import org.soaplab.domain.utils.IngredientsExampleData;

class FatTest {

	@Test
	void testEntityWithSameIdIsEqual() {
		final UUID uuid = UUID.randomUUID();
		final Fat fat1 = Fat.builder().id(uuid).name("test").build();
		final Fat fat2 = Fat.builder().id(uuid).name("test").build();
		assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdIsButDifferentValuesEqual() {
		final UUID uuid = UUID.randomUUID();
		final Fat fat1 = Fat.builder().id(uuid).name("test").build();
		final Fat fat2 = Fat.builder().id(uuid).name("xxx").build();
		assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithDifferentIdIsNotEqual() {
		final Fat fat1 = Fat.builder().id(UUID.randomUUID()).name("test").build();
		final Fat fat2 = Fat.builder().id(UUID.randomUUID()).name("test").build();
		assertThat(fat1).isNotEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdButDifferentTypeNotEqual() {
		final UUID uuid = UUID.randomUUID();
		final Fat fat1 = Fat.builder().id(uuid).name("test").build();
		final Acid acid = Acid.builder().id(uuid).name("test").build();
		assertThat(fat1).isNotEqualTo(acid);
	}

	@Test
	void testDeepCloning() {
		final Fat originalFat = IngredientsExampleData.getCoconutOilBuilder().build();
		final Fat clone = originalFat.getCopyBuilder().build();
		FatAssert.assertThat(originalFat).isDeepEqualTo(clone);
	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
