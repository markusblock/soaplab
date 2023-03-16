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
		return multiply(divide(pricePer100g, BigDecimal.valueOf(100)), weight.getWeight());
	}

	public Price calculatePricePer100g(Price totalCost, Weight totalWeight) {

		final BigDecimal result = totalCost.getValue().divide(totalWeight.getWeight(), MathContext.DECIMAL32);
		return createNewPrice(multiply(result, BigDecimal.valueOf(100)));
		
		//TODO add test
		// return multiply(divide(totalCost, totalWeight.getWeight()),
		// BigDecimal.valueOf(100));
	}

	public Price multiply(Price price, BigDecimal multiplicator) {
		return createNewPrice(price.getValue().multiply(multiplicator, MathContext.DECIMAL32));
	}

	private BigDecimal multiply(BigDecimal bd1, BigDecimal bd2) {
		return bd1.multiply(bd2);
	}

	public Price divide(Price divident, BigDecimal divisor) {
		return createNewPrice(divident.getValue().divide(divisor, MathContext.DECIMAL32));
	}

	public BigDecimal divide(Price divident, Price divisor) {
		return adjustResultScale(divident.getValue().divide(divisor.getValue(), MathContext.DECIMAL32));
	}

	public Price calculatePercentage(Price price, Percentage percentage) {
		return multiply(price, percentage.getNumber().divide(BigDecimal.valueOf(100), MathContext.DECIMAL32));
	}

	public Price subtract(Price price, Price subtractor) {
		return createNewPrice(price.getValue().subtract(subtractor.getValue()));
	}

	public Price plus(Price price, Price summand) {
		return createNewPrice(price.getValue().add(summand.getValue()));
	}
}
