
package org.soaplab.domain;

import java.math.BigDecimal;

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
@SuperBuilder(toBuilder = true)
public class Liquid extends Ingredient {

	private static final long serialVersionUID = 1L;

	/**
	 * sapNaoh value is the numeric value that allow you to calculate the precise
	 * amount of sodium hydroxide (NaOH) required to fully saponify a given weight
	 * of oil/s.
	 */
	private BigDecimal sapNaoh;

	/**
	 * {@link LiquidBuilder} adds custom methods to lombok generated builder class.
	 */
	public static abstract class LiquidBuilder<C extends Liquid, B extends LiquidBuilder<C, B>>
			extends IngredientBuilder<C, B> {

		public B sapNaoh(BigDecimal sapNaoh) {
			this.sapNaoh = sapNaoh;
			return self();
		}

		public B sapNaoh(double sapNaoh) {
			sapNaoh(BigDecimal.valueOf(sapNaoh));
			return self();
		}
	}

	@Override
	public LiquidBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder();
	}
}
