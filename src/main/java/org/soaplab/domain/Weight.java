package org.soaplab.domain;

import java.io.Serializable;
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
@ImplicitEntity
public class Weight implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal weight = BigDecimal.valueOf(0);
	private WeightUnit unit = WeightUnit.GRAMS;

	public static Weight of(int weight, WeightUnit unit) {
		return new Weight(BigDecimal.valueOf(weight), unit);
	}

	public static Weight of(double weight, WeightUnit unit) {
		return new Weight(BigDecimal.valueOf(weight), unit);
	}

	public static Weight ofGrams(double weight) {
		return new Weight(BigDecimal.valueOf(weight), WeightUnit.GRAMS);
	}
}
