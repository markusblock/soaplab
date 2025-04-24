
package org.soaplab.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ImplicitEntity
@FieldNameConstants
public class RecipeEntry<T extends Ingredient> extends Entity {

	private static final long serialVersionUID = 1L;

	/**
	 * Percentage of the {@link Ingredient}.
	 */
	@Default
	private Percentage percentage = Percentage.of(0);

	/**
	 * Weight in grams. Value >0. Value will be calculated.
	 */
	@Default
	private Weight weight = Weight.ofGrams(0);

	/**
	 * The cost for this amount of {@link Ingredient}. Value will be calculated.
	 */
	@Default
	private Price price = Price.of(0);

	/**
	 * Optional notes on the recipe entry
	 */
	private String notes;

	/**
	 * The {@link Ingredient}.
	 */
	private T ingredient;

	@Override
	public RecipeEntryBuilder<T, ?, ?> getCopyBuilder() {
		return this.toBuilder().ingredient((T) this.ingredient.getCopyBuilder().build());
	}
}
