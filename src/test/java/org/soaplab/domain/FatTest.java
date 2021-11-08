package org.soaplab.domain;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FatTest {

	@Test
	void testEntityWithSameIdIsEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		UUID uuid = UUID.randomUUID();
		fat1.setId(uuid);
		Fat fat2 = Fat.builder().name("test").build();
		fat2.setId(uuid);
		Assertions.assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdIsButDifferentValuesEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		UUID uuid = UUID.randomUUID();
		fat1.setId(uuid);
		Fat fat2 = Fat.builder().name("xxx").build();
		fat2.setId(uuid);
		Assertions.assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithDifferentIdIsNotEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		fat1.setId(UUID.randomUUID());
		Fat fat2 = Fat.builder().name("test").build();
		fat2.setId(UUID.randomUUID());
		Assertions.assertThat(fat1).isNotEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdButDifferentTypeNotEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		UUID uuid = UUID.randomUUID();
		fat1.setId(uuid);
		Acid acid = Acid.builder().name("test").build();
		acid.setId(uuid);
		Assertions.assertThat(fat1).isNotEqualTo(acid);
	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
