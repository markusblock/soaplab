package org.mysoap.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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

	public void validate(Double number) {
		if (number < 0d) {
			throw new NumberFormatException("Percentage not allowed to be < 0%");
		}
		if (number > 100d) {
			throw new NumberFormatException("Percentage not allowed to be > 100%");
		}
	}

}
