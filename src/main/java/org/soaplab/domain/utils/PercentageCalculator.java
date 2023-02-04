package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;

public class PercentageCalculator {

	private final MathContext mathContext;

	public PercentageCalculator(MathContext mathContext) {
		super();
		this.mathContext = mathContext;
	}

	public PercentageCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}

	public BigDecimal divide(Percentage divident, Percentage divisor) {
		return divident.getNumber().divide(divisor.getNumber(), mathContext);
	}
}
