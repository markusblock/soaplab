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
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Price implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Currency is € Euro.
	 */
	private BigDecimal value;

	public static Price of(double value) {
		return new Price(BigDecimal.valueOf(value));
	}

	public Price calculatePercentage(Percentage percentage) {
		return new Price(value.multiply(percentage.getNumber().divide(BigDecimal.valueOf(100))));
	}

	public Price multiply(BigDecimal multiplicator) {
		return new Price(value.multiply(multiplicator));
	}

	public Price divide(BigDecimal divisor) {
		return new Price(value.divide(divisor));
	}

	public Price subtract(Price subtractor) {
		return new Price(value.subtract(subtractor.getValue()));
	}

	public Price plus(Price summand) {
		return new Price(value.add(summand.getValue()));
	}
}
