
package org.soaplab.domain;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldNameConstants
public class Acid extends Ingredient {

	private static final long serialVersionUID = 1L;

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

	@Override
	public AcidBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder();
	}
}
