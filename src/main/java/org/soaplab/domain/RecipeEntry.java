
package org.soaplab.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true, builderMethodName = "recipeEntryBuilder")
public class RecipeEntry<T extends Ingredient> extends Entity {

	/**
	 * Percentage of the {@link Ingredient}.
	 */
	private Percentage percentage;

	/**
	 * The {@link Ingredient}.
	 */
	private T ingredient;

	@Override
	public RecipeEntry<T> getClone() {
		return new RecipeEntry<T>(
				this.toBuilder().percentage(this.percentage).ingredient((T) this.ingredient.getClone()));
	}

}
