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
public class Percentage implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal number = BigDecimal.valueOf(0);

	public static Percentage of(int number) {
		return new Percentage(BigDecimal.valueOf(number));
	}

	public static Percentage of(double number) {
		return new Percentage(BigDecimal.valueOf(number));
	}

	public void validate(Double number) {
		if (number < 0d) {
			throw new NumberFormatException("Percentage not allowed to be < 0%");
		}
		if (number > 100d) {
			throw new NumberFormatException("Percentage not allowed to be > 100%");
		}
	}

	public static boolean isGreaterThanZero(Percentage percentage) {
		return percentage != null && percentage.getNumber().compareTo(BigDecimal.ZERO) > 0;
	}
}
