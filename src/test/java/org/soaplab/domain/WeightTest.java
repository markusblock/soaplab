package org.soaplab.domain;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.soaplab.assertions.WeightAssert;

class WeightTest {

	@Test
	void plus() {
		WeightAssert.assertThat(createWeight(0).plus(createWeight(0))).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(createWeight(1.5).plus(createWeight(0.5))).isEqualToWeightInGrams(2);
		WeightAssert.assertThat(createWeight(1.9999).plus(createWeight(0.0002))).isEqualToWeightInGrams(2.0001);
	}

	@Test
	void multiply() {
		WeightAssert.assertThat(createWeight(0).multiply(BigDecimal.ZERO)).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(createWeight(1.5).multiply(BigDecimal.valueOf(2))).isEqualToWeightInGrams(3);
		WeightAssert.assertThat(createWeight(1.9999).multiply(BigDecimal.valueOf(0.5d)))
				.isEqualToWeightInGrams(0.99995);
	}

	@Test
	void percentage() {
		WeightAssert.assertThat(createWeight(0).calculatePercentage(Percentage.of(0))).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(createWeight(100).calculatePercentage(Percentage.of(10))).isEqualToWeightInGrams(10);
		WeightAssert.assertThat(createWeight(100).calculatePercentage(Percentage.of(0.5))).isEqualToWeightInGrams(0.5);

	}

	private Weight createWeight(int weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

	private Weight createWeight(double weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

}
