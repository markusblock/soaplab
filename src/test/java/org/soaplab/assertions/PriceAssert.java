package org.soaplab.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Price;

public class PriceAssert extends AbstractAssert<PriceAssert, Price> {

	public PriceAssert(Price actual) {
		super(actual, PriceAssert.class);
	}

	public static PriceAssert assertThat(Price actual) {
		return new PriceAssert(actual);
	}

	public PriceAssert isEqualTo(Price expected) {

		isNotNull();

		Assertions.assertThat(actual.getValue()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expected.getValue());

		return this;
	}

	public PriceAssert isEqualToValue(double price) {
		return isEqualTo(Price.of(price));
	}
}
