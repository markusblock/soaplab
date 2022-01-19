package org.soaplab.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

public class WeightAssert extends AbstractAssert<WeightAssert, Weight> {

	public WeightAssert(Weight actual) {
		super(actual, WeightAssert.class);
	}

	public static WeightAssert assertThat(Weight actual) {
		return new WeightAssert(actual);
	}

	public WeightAssert isEqualTo(Weight expected) {

		isNotNull();

		Assertions.assertThat(actual.getUnit()).isEqualTo(expected.getUnit());
		Assertions.assertThat(actual.getWeight()).usingComparator(new BigDecimalComparator())
				.isEqualTo(expected.getWeight());

		return this;
	}

	public WeightAssert isEqualToWeightInGrams(int weight) {
		return isEqualTo(Weight.of(weight, WeightUnit.GRAMS));
	}

	public WeightAssert isEqualToWeightInGrams(double weight) {
		return isEqualTo(Weight.of(weight, WeightUnit.GRAMS));
	}

}
