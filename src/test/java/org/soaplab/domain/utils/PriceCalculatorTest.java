package org.soaplab.domain.utils;

import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.assertions.PriceAssert;
import org.soaplab.domain.Price;
import org.soaplab.domain.Weight;

class PriceCalculatorTest {

	private final MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
	private PriceCalculator calc;

	@BeforeEach
	void beforeEach() {
		calc = new PriceCalculator(mathContext);
	}

	@Test
	void calculatePricePer100g() {
		PriceAssert.assertThat(calc.calculatePricePer100g(Price.of(0.75), Weight.ofGrams(12.15))).isEqualToValue(6.17);
	}

	@Test
	void calculatePricePerWeight() {
		PriceAssert.assertThat(calc.calculatePriceForWeight(Price.of(0.75), Weight.ofGrams(12.15)))
				.isEqualToValue(0.09);
	}
}
