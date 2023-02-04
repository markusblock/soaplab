package org.soaplab.domain.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.soaplab.domain.Percentage;
import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

public class WeightCalculator {

	private final MathContext mathContext;

	public WeightCalculator(MathContext mathContext) {
		super();
		this.mathContext = mathContext;
	}

	public WeightCalculator(int decimalPlaces, RoundingMode roundingMode) {
		this(new MathContext(decimalPlaces, roundingMode));
	}

	private Weight createNewWeight(BigDecimal bd, WeightUnit unit) {
		return new Weight(bd.setScale(mathContext.getPrecision(), mathContext.getRoundingMode()), unit);
	}

	public Weight calculatePercentage(Weight weight, Percentage... percentages) {
		BigDecimal bd1 = weight.getWeight();
		for (final Percentage percentage : percentages) {
			bd1 = bd1.multiply(percentage.getNumber().divide(BigDecimal.valueOf(100)));
		}
		return createNewWeight(bd1, weight.getUnit());
	}

	public Weight multiply(Weight weight, BigDecimal... multiplicators) {
		BigDecimal bd1 = weight.getWeight();
		for (final BigDecimal multiplicator : multiplicators) {
			bd1 = bd1.multiply(multiplicator, mathContext);
		}
		return createNewWeight(bd1, weight.getUnit());
	}

	public Weight divide(Weight weight, BigDecimal divisor) {
		return new Weight(weight.getWeight().divide(divisor, mathContext), weight.getUnit());
	}

	public Weight subtract(Weight weight, Weight... subtractors) {
		BigDecimal bd1 = weight.getWeight();
		for (final Weight subtractor : subtractors) {
			bd1 = bd1.subtract(subtractor.getWeight(), mathContext);
		}
		return createNewWeight(bd1, weight.getUnit());
	}

	public Weight plus(Weight weight, Weight... summands) {
		BigDecimal bd1 = weight.getWeight();
		for (final Weight weightToAdd : summands) {
			bd1 = bd1.add(weightToAdd.getWeight(), mathContext);
		}
		return createNewWeight(bd1, weight.getUnit());
	}
}
