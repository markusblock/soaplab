
package org.soaplab.domain;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
public class CalculatedRecipeEntry<T extends Ingredient> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Weight in grams. Value >0;
	 */
	private Weight weight;

	/**
	 * The cost for this amount of {@link Ingredient}.
	 */
	private Price price;

	/**
	 * The {@link Ingredient}.
	 */
	private T ingredient;
}
