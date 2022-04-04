package org.soaplab.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Percentage;

public class PercentageAssert extends AbstractAssert<PercentageAssert, Percentage> {

	public PercentageAssert(Percentage actual) {
		super(actual, PercentageAssert.class);
	}

	public static PercentageAssert assertThat(Percentage actual) {
		return new PercentageAssert(actual);
	}

	public PercentageAssert isDeepEqualTo(Percentage expected) {

		isNotNull();

		Assertions.assertThat(actual.getNumber()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expected.getNumber());

		return this;
	}
}
