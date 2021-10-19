package org.soaplab.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;

class FatTest {

	@Test
	void testEntityWithSameIdIsEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		fat1.setId(1l);
		Fat fat2 = Fat.builder().name("test").build();
		fat2.setId(1l);
		Assertions.assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdIsButDifferentValuesEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		fat1.setId(1l);
		Fat fat2 = Fat.builder().name("xxx").build();
		fat2.setId(1l);
		Assertions.assertThat(fat1).isEqualTo(fat2);
	}

	@Test
	void testEntityWithDifferentIdIsNotEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		fat1.setId(1l);
		Fat fat2 = Fat.builder().name("test").build();
		fat2.setId(2l);
		Assertions.assertThat(fat1).isNotEqualTo(fat2);
	}

	@Test
	void testEntityWithSameIdIsButDifferentTypeNotEqual() {
		Fat fat1 = Fat.builder().name("test").build();
		fat1.setId(1l);
		Acid acid = Acid.builder().name("test").build();
		acid.setId(1l);
		Assertions.assertThat(fat1).isNotEqualTo(acid);
	}

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Fat.class).verify();
//	}

}
