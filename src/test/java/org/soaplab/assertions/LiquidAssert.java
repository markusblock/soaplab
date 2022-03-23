package org.soaplab.assertions;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Liquid;

public class LiquidAssert extends IngredientAssert {

	public LiquidAssert(Liquid actual) {
		super(actual, LiquidAssert.class);
	}

	public static LiquidAssert assertThat(Liquid actual) {
		return new LiquidAssert(actual);
	}

	@Override
	public LiquidAssert isDeepEqualTo(Ingredient expected) {

		isNotNull();

		super.isDeepEqualTo(expected);

		Liquid expectedLiquid = (Liquid) expected;
		Assertions.assertThat(getActual().getSapNaoh()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expectedLiquid.getSapNaoh());

		return this;
	}

	private Liquid getActual() {
		return (Liquid) actual;
	}
}
