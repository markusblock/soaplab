package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalCalculator {

	private final MathContext resultMathContext;
	private final MathContext internalMathContext = MathContext.DECIMAL32;

	public BigDecimalCalculator() {
		resultMathContext = MathContext.DECIMAL32;
	}

	public BigDecimalCalculator(MathContext resultMathContext) {
		super();
		this.resultMathContext = resultMathContext;
	}

	public BigDecimalCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}

	public BigDecimal multiply(BigDecimal bd1, BigDecimal bd2) {
		return adjustResultScale(bd1.multiply(bd2, internalMathContext), resultMathContext);
	}

	public BigDecimal divide(BigDecimal bd1, BigDecimal bd2) {
		return adjustResultScale(bd1.divide(bd2, internalMathContext), resultMathContext);
	}

	public BigDecimal subtract(BigDecimal bd, BigDecimal subtractor) {
		return adjustResultScale(bd.subtract(subtractor, internalMathContext), resultMathContext);
	}

	public BigDecimal plus(BigDecimal bd, BigDecimal summand) {
		return adjustResultScale(bd.add(summand, internalMathContext), resultMathContext);
	}

	public BigDecimal adjustResultScale(BigDecimal bd, MathContext mathContext) {
		return bd.setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
	}
}
