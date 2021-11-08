package org.soaplab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.WeightUnit;
import org.soaplab.domain.Weight;

class WeightTest {

	@Test
	void plus() {
		assertThat(createWeightInGrams(0).plus(createWeightInGrams(0))).isEqualTo(createWeightInGrams(0));
		assertThat(createWeight(1.5).plus(createWeight(0.5))).isEqualTo(createWeightInGrams(2));
		assertThat(createWeight(1.9999).plus(createWeight(0.0002))).isEqualTo(createWeight(2.0001));
	}

	@Test
	void multiply() {
		assertThat(createWeightInGrams(0).multiply(0d)).isEqualTo(createWeightInGrams(0));
		assertThat(createWeight(1.5).multiply(2d)).isEqualTo(createWeightInGrams(3));
		assertThat(createWeight(1.9999).multiply(0.5)).isEqualTo(createWeight(0.99995));
	}

	@Test
	void percentage() {
		assertThat(createWeightInGrams(0).calculatePercentage(Percentage.of(0))).isEqualTo(createWeightInGrams(0));
		assertThat(createWeightInGrams(100).calculatePercentage(Percentage.of(10))).isEqualTo(createWeightInGrams(10));
		assertThat(createWeightInGrams(100).calculatePercentage(Percentage.of(0.5))).isEqualTo(createWeight(0.5));

	}

	private Weight createWeightInGrams(int weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

	private Weight createWeight(double weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

}
