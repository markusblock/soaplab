package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;
import org.soaplab.domain.Weight;

public class PriceCalculator {

	private final MathContext resultMathContext;
	private final BigDecimalCalculator calculator;

	public PriceCalculator(MathContext resultMathContext) {
		super();
		this.resultMathContext = resultMathContext;
		this.calculator = new BigDecimalCalculator();
	}

	public PriceCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}

	private Price createNewPrice(BigDecimal bd) {
		return new Price(bd.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()));
	}

	public Price calculatePriceForWeight(Price pricePer100g, Weight weight) {
		return createNewPrice(
				calculator.multiply(calculator.divide(pricePer100g.getValue(), BigDecimal.valueOf(100)),
						weight.getWeight()));
	}

	public Price calculatePricePer100g(Price totalCost, Weight totalWeight) {
		return createNewPrice(calculator.multiply(calculator.divide(totalCost.getValue(), totalWeight.getWeight()),
				BigDecimal.valueOf(100)));
	}

	public Price multiply(Price price, BigDecimal multiplicator) {
		return createNewPrice(calculator.multiply(price.getValue(), multiplicator));
	}

	public Price divide(Price divident, BigDecimal divisor) {
		return createNewPrice(calculator.divide(divident.getValue(), divisor));
	}

	public Price calculatePercentage(Price price, Percentage percentage) {
		return multiply(price, calculator.divide(percentage.getNumber(), BigDecimal.valueOf(100)));
	}

	public Price subtract(Price price, Price subtractor) {
		return createNewPrice(calculator.subtract(price.getValue(), subtractor.getValue()));
	}

	public Price plus(Price price, Price summand) {
		return createNewPrice(calculator.plus(price.getValue(), summand.getValue()));
	}
}
