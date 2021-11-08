
package org.soaplab.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder
public class Liquid extends Ingredient {
	/**
	 * sapNaoh value is the numeric value that allow you to calculate the precise
	 * amount of sodium hydroxide (NaOH) required to fully saponify a given weight
	 * of oil/s.
	 */
	private Double sapNaoh;
}
