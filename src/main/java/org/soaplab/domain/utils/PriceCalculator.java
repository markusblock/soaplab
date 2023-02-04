package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;
import org.soaplab.domain.Weight;

public class PriceCalculator {

	private final MathContext resultMathContext;

	public PriceCalculator(MathContext resultMathContext) {
		super();
		this.resultMathContext = resultMathContext;
	}

	public PriceCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}
	
	private Price createNewPrice(BigDecimal bd) {
		return new Price(bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()));
	}
	
	private BigDecimal adjustResultScale(BigDecimal bd) {
		return bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
	}

	public Price calculatePriceForWeight(Price pricePer100g, Weight weight) {
		return createNewPrice(multiply(divide(pricePer100g, BigDecimal.valueOf(100)),weight.getWeight()));
	}

	public BigDecimal multiply(Price price, BigDecimal multiplicator) {
		return adjustResultScale(price.getValue().multiply(multiplicator, MathContext.DECIMAL32));
	}
	
	public Price divide(Price divident, BigDecimal divisor) {
		return createNewPrice(divident.getValue().divide(divisor, MathContext.DECIMAL32));
	}
	
	public BigDecimal divide(Price divident, Price divisor) {
		return adjustResultScale(divident.getValue().divide(divisor.getValue(), MathContext.DECIMAL32));
	}
	
	public Price calculatePercentage(Price price,Percentage percentage) {
		return createNewPrice(multiply(price, percentage.getNumber().divide(BigDecimal.valueOf(100),MathContext.DECIMAL32)));
	}

	public Price subtract(Price price,Price subtractor) {
		return createNewPrice(price.getValue().subtract(subtractor.getValue()));
	}

	public Price plus(Price price, Price summand) {
		return createNewPrice(price.getValue().add(summand.getValue()));
	}
}
