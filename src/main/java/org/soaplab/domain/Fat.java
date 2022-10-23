
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
public class Fat extends Ingredient {

	/**
	 * {@link FatBuilder} adds custom methods to lombok generated builder class.
	 */
	public static abstract class FatBuilder<C extends Fat, B extends FatBuilder<C, B>> extends IngredientBuilder<C, B> {

		public B sapNaoh(BigDecimal sapNaoh) {
			this.sapNaoh = sapNaoh;
			return self();
		}

		public B sapNaoh(double sapNaoh) {
			sapNaoh(BigDecimal.valueOf(sapNaoh));
			return self();
		}
	}

	private static final long serialVersionUID = 1L;
	/**
	 * sapNaoh value is the numeric value that allow you to calculate the precise
	 * amount of sodium hydroxide (NaOH) required to fully saponify a given weight
	 * of oil/s.
	 */
	private BigDecimal sapNaoh;
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

	@Override
	public Fat getClone() {
		return new Fat(this.toBuilder());
	}

	public FatBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder();
	}
}
