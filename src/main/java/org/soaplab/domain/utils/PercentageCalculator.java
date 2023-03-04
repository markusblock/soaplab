package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;

public class PercentageCalculator {

	private final MathContext resultMathContext;

	public PercentageCalculator(MathContext resultMathContext) {
		super();
		this.resultMathContext = resultMathContext;
	}

	public PercentageCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}
	
	private Percentage createNewPercentage(BigDecimal bd) {
		return new Percentage(bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()));
	}
	
	private BigDecimal adjustResultScale(BigDecimal bd) {
		return bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
	}

	public BigDecimal divide(Percentage divident, Percentage divisor) {
		return adjustResultScale(divident.getNumber().divide(divisor.getNumber(), MathContext.DECIMAL32));
	}
}
