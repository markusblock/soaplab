package org.soaplab.domain.utils;

import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.assertions.WeightAssert;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Weight;

class WeightCalculatorTest {

	private final MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
	private WeightCalculator calc;

	@BeforeEach
	void beforeEach() {
		calc = new WeightCalculator(mathContext);
	}

	@Test
	void calculatePercentage() {

		WeightAssert.assertThat(calc.calculatePercentage(Weight.ofGrams(0), Percentage.of(0)))
				.isEqualToWeightInGrams(0);
		WeightAssert.assertThat(calc.calculatePercentage(Weight.ofGrams(1000), Percentage.of(10)))
				.isEqualToWeightInGrams(100);
	}
}
