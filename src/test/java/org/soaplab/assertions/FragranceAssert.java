package org.soaplab.assertions;

import org.assertj.core.api.Assertions;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Ingredient;

public class FragranceAssert extends IngredientAssert {

	public FragranceAssert(Fragrance actual) {
		super(actual, FragranceAssert.class);
	}

	public static FragranceAssert assertThat(Fragrance actual) {
		return new FragranceAssert(actual);
	}

	@Override
	public FragranceAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		Fragrance expectedFragrance = (Fragrance) expected;
		Assertions.assertThat(getActual().getType()).isEqualTo(expectedFragrance.getType());

		return this;
	}

	private Fragrance getActual() {
		return (Fragrance) actual;
	}
}
