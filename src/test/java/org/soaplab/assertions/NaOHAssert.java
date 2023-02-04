package org.soaplab.assertions;

import org.soaplab.domain.Ingredient;
import org.soaplab.domain.NaOH;

public class NaOHAssert extends IngredientAssert {

	public NaOHAssert(NaOH actual) {
		super(actual, NaOHAssert.class);
	}

	public static NaOHAssert assertThat(NaOH actual) {
		return new NaOHAssert(actual);
	}

	@Override
	public NaOHAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		return this;
	}
}
