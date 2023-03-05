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
@ImplicitEntity
public class Price implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Currency is € Euro.
	 */
	private BigDecimal value;

	public static Price of(double value) {
		return new Price(BigDecimal.valueOf(value));
	}
}
