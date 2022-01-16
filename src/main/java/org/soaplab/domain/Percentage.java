package org.soaplab.domain;

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
public class Percentage {

	private Double number;

	public Percentage(int number) {
		this(Double.valueOf(number));
	}

	public static Percentage of(int number) {
		return new Percentage(number);
	}

	public static Percentage of(double number) {
		return new Percentage(number);
	}

	public Percentage minus(Percentage percentage) {
		return Percentage.of(number - percentage.getNumber());
	}

	public Percentage plus(Percentage percentage) {
		return Percentage.of(number + percentage.getNumber());
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
		return percentage != null && percentage.number > 0d;
	}
}
