package org.soaplab.assertions;

import org.soaplab.domain.Ingredient;
import org.soaplab.domain.KOH;

public class KOHAssert extends IngredientAssert {

	public KOHAssert(KOH actual) {
		super(actual, KOHAssert.class);
	}

	public static KOHAssert assertThat(KOH actual) {
		return new KOHAssert(actual);
	}

	@Override
	public KOHAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		final KOH expectedKOH = (KOH) expected;
		PercentageAssert.assertThat(getActual().getKohPurity()).isDeepEqualTo(expectedKOH.getKohPurity());

		return this;
	}

	private KOH getActual() {
		return (KOH) actual;
	}
}
