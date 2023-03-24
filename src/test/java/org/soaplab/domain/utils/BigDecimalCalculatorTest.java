package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BigDecimalCalculatorTest {

	private final MathContext mathContext = new MathContext(4, RoundingMode.HALF_UP);
	private BigDecimalCalculator calc;

	@BeforeEach
	public void beforeEach() {
		calc = new BigDecimalCalculator(mathContext);
	}

	@Test
	void plus() {
		Assertions.assertThat(calc.plus(bigDec(0), bigDec(0))).isZero();
		Assertions.assertThat(calc.plus(bigDec(1), bigDec(2))).isEqualByComparingTo("3");
		Assertions.assertThat(calc.plus(bigDec(1.5), bigDec(0.5))).isEqualByComparingTo("2");
		Assertions.assertThat(calc.plus(bigDec(1), bigDec(0.01))).isEqualByComparingTo("1.01");
		Assertions.assertThat(calc.plus(bigDec(1), bigDec(0.001))).isEqualByComparingTo("1.001");
		Assertions.assertThat(calc.plus(bigDec(1), bigDec(0.0001))).isEqualByComparingTo("1.0001");
		Assertions.assertThat(calc.plus(bigDec(1), bigDec(0.00001))).isEqualByComparingTo("1");

		Assertions.assertThat(calc.plus(bigDec(1.999), bigDec(0.002))).isEqualByComparingTo("2.001");
		Assertions.assertThat(calc.plus(bigDec(1.9999), bigDec(0.0002))).isEqualByComparingTo("2.0001");
		Assertions.assertThat(calc.plus(bigDec(1.99999), bigDec(0.00002))).isEqualByComparingTo("2");
	}

	@Test
	void subtract() {
		Assertions.assertThat(calc.subtract(bigDec(0), bigDec(0))).isZero();
		Assertions.assertThat(calc.subtract(bigDec(0.99995), bigDec(0.00005))).isEqualByComparingTo("0.9999");
		Assertions.assertThat(calc.subtract(bigDec(1), bigDec(0.00005))).isEqualByComparingTo("1");
	}

	@Test
	void multiply() {
		Assertions.assertThat(calc.multiply(bigDec(0), bigDec(0))).isZero();
		Assertions.assertThat(calc.multiply(bigDec(1.5), bigDec(2))).isEqualByComparingTo("3");
		Assertions.assertThat(calc.multiply(bigDec(1.999), bigDec(0.5))).isEqualByComparingTo("0.9995");
		Assertions.assertThat(calc.multiply(bigDec(1.9999), bigDec(0.5))).isEqualByComparingTo("1");
		Assertions.assertThat(calc.multiply(bigDec(1.9999), bigDec(2))).isEqualByComparingTo("3.9998");
		Assertions.assertThat(calc.multiply(bigDec(1.99999), bigDec(0.5))).isEqualByComparingTo("1");
	}

	@Test
	void divide() {
		Assertions.assertThat(calc.divide(bigDec(0), bigDec(2))).isEqualByComparingTo("0");
		Assertions.assertThat(calc.divide(bigDec(0.1), bigDec(0.9))).isEqualByComparingTo("0.1111");
	}

	private BigDecimal bigDec(double d) {
		return BigDecimal.valueOf(d);
	}
}
