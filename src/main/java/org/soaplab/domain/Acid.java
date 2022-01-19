
package org.soaplab.domain;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder
public class Acid extends Ingredient {

	private BigDecimal sapNaoh;

	/**
	 * {@link AcidBuilder} adds custom methods to lombok generated builder class.
	 */
	public static abstract class AcidBuilder<C extends Acid, B extends AcidBuilder<C, B>>
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
}
