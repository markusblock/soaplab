package org.soaplab.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

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

	private BigDecimal value;
	private Currency currency;
}
