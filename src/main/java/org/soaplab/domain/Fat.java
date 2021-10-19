
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
public class Fat extends Ingredient {
	/**
	 * sapNaoh value is the numeric value that allow you to calculate the precise
	 * amount of sodium hydroxide (NaOH) required to fully saponify a given weight
	 * of oil/s.
	 */
	private Double sapNaoh;
	/**
	 * sapNaoh value is the numeric value that allow you to calculate the precise
	 * amount of potassium hydroxide (KOH) required to fully saponify a given weight
	 * of oil/s.
	 */
	private Double sapKoh;
	private Integer lauric;
	private Integer myristic;
	private Integer palmitic;
	private Integer stearic;
	private Integer ricinoleic;
	private Integer oleic;
	private Integer linoleic;
	private Integer linolenic;
	private Integer iodine;
	private Integer ins;

}
