package org.soaplab.assertions;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Acid;
import org.soaplab.domain.Ingredient;

public class AcidAssert extends IngredientAssert {

	public AcidAssert(Acid actual) {
		super(actual, AcidAssert.class);
	}

	public static AcidAssert assertThat(Acid actual) {
		return new AcidAssert(actual);
	}

	@Override
	public AcidAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		Acid expectedAcid = (Acid) expected;
		Assertions.assertThat(getActual().getSapNaoh()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expectedAcid.getSapNaoh());

		return this;
	}

	private Acid getActual() {
		return (Acid) actual;
	}
}
