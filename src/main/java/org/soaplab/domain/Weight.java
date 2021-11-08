package org.soaplab.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Weight {

	private Double weight;
	private WeightUnit unit;

	public Weight(int weight, WeightUnit unit) {
		this(Double.valueOf(weight), unit);
	}

	public static Weight of(int weight, WeightUnit unit) {
		return new Weight(weight, unit);
	}

	public static Weight of(double weight, WeightUnit unit) {
		return new Weight(weight, unit);
	}

	public Weight calculatePercentage(Percentage percentage) {
		return new Weight(weight * percentage.getNumber() / 100, unit);
	}

	public Weight multiply(Double multiplicator) {
		return new Weight(weight * multiplicator, unit);
	}

	public Weight divide(Double divisor) {
		return new Weight(weight / divisor, unit);
	}

	public Weight subtract(Weight subtractor) {
		return new Weight(weight - subtractor.getWeight(), unit);
	}

	public Weight plus(Weight summand) {
		return new Weight(weight + summand.getWeight(), unit);
	}

}
