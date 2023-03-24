package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;

public class PercentageCalculator {

	private final MathContext resultMathContext;
	private final BigDecimalCalculator calculator;

	public PercentageCalculator(MathContext resultMathContext) {
		super();
		this.resultMathContext = resultMathContext;
		this.calculator = new BigDecimalCalculator();
	}

	public PercentageCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}
	
	private Percentage createNewPercentage(BigDecimal bd) {
		return new Percentage(bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()));
	}

	public BigDecimal divide(Percentage divident, Percentage divisor) {
		return calculator.adjustResultScale(calculator.divide(divident.getNumber(), divisor.getNumber()),
				resultMathContext);
	}
}
