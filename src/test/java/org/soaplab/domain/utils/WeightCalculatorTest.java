package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

class WeightCalculatorTest {

	private final MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
	private WeightCalculator calc;

	@BeforeEach
	public void beforeEach() {
		calc = new WeightCalculator(mathContext);
	}

	@Test
	void plus() {
		WeightAssert.assertThat(calc.plus(createWeight(0), createWeight(0))).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(calc.plus(createWeight(1.5), createWeight(0.5))).isEqualToWeightInGrams(2);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.01))).isEqualToWeightInGrams(1.01);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.001))).isEqualToWeightInGrams(1.001);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.0001))).isEqualToWeightInGrams(1.0001);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.00001))).isEqualToWeightInGrams(1);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.5))).isEqualToWeightInGrams(1.5);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.005))).isEqualToWeightInGrams(1.005);
		WeightAssert.assertThat(calc.plus(createWeight(1), createWeight(0.0005))).isEqualToWeightInGrams(1.0005);
		WeightAssert.assertThat(calc.plus(createWeight(1.999), createWeight(0.002))).isEqualToWeightInGrams(2.001);
		WeightAssert.assertThat(calc.plus(createWeight(1.9999), createWeight(0.0002))).isEqualToWeightInGrams(2.0001);
		WeightAssert.assertThat(calc.plus(createWeight(1.99999), createWeight(0.00002))).isEqualToWeightInGrams(2);
	}

	@Test
	void multiply() {
		WeightAssert.assertThat(calc.multiply(createWeight(0), BigDecimal.ZERO)).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(calc.multiply(createWeight(1.5), BigDecimal.valueOf(2))).isEqualToWeightInGrams(3);
		WeightAssert.assertThat(calc.multiply(createWeight(1.999), BigDecimal.valueOf(0.5d)))
				.isEqualToWeightInGrams(0.9995);
		WeightAssert.assertThat(calc.multiply(createWeight(1.9999), BigDecimal.valueOf(0.5d)))
				.isEqualToWeightInGrams(1);
		WeightAssert.assertThat(calc.multiply(createWeight(1.9999), BigDecimal.valueOf(2d)))
		.isEqualToWeightInGrams(3.9998);
		WeightAssert.assertThat(calc.multiply(createWeight(1.99999), BigDecimal.valueOf(0.5d)))
				.isEqualToWeightInGrams(1);
	}

	@Test
	void percentage() {
		WeightAssert.assertThat(calc.calculatePercentage(createWeight(0), Percentage.of(0))).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(calc.calculatePercentage(createWeight(100), Percentage.of(10)))
				.isEqualToWeightInGrams(10);
		WeightAssert.assertThat(calc.calculatePercentage(createWeight(100), Percentage.of(0.3)))
				.isEqualToWeightInGrams(0.3);

	}
	
	@Test
	void divide() {
		WeightAssert.assertThat(calc.divide(createWeight(0), BigDecimal.valueOf(0.5d))).isEqualToWeightInGrams(0);
		WeightAssert.assertThat(calc.divide(createWeight(0.1d), BigDecimal.valueOf(0.9d))).isEqualToWeightInGrams(0.1111);
	}

	private Weight createWeight(int weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

	private Weight createWeight(double weight) {
		return Weight.of(weight, WeightUnit.GRAMS);
	}

}
