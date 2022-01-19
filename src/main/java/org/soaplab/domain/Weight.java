package org.soaplab.domain;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Weight {

	private BigDecimal weight;
	private WeightUnit unit;

	public Weight(int weight, WeightUnit unit) {
		this(BigDecimal.valueOf(weight), unit);
	}

	public static Weight of(int weight, WeightUnit unit) {
		return new Weight(weight, unit);
	}

	public static Weight of(double weight, WeightUnit unit) {
		return new Weight(BigDecimal.valueOf(weight), unit);
	}

	public Weight calculatePercentage(Percentage percentage) {
		return new Weight(weight.multiply(percentage.getNumber().divide(BigDecimal.valueOf(100))), unit);
	}

	public Weight multiply(BigDecimal multiplicator) {
		return new Weight(weight.multiply(multiplicator), unit);
	}

	public Weight divide(BigDecimal divisor) {
		return new Weight(weight.divide(divisor), unit);
	}

	public Weight subtract(Weight subtractor) {
		return new Weight(weight.subtract(subtractor.getWeight()), unit);
	}

	public Weight plus(Weight summand) {
		return new Weight(weight.add(summand.getWeight()), unit);
	}

}