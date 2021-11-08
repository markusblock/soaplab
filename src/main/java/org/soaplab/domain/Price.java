package org.soaplab.domain;

import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Price {

	private Double value;
	private Currency currency;
}
