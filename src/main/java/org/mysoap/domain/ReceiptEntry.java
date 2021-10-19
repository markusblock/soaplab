
package org.mysoap.domain;

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
public class ReceiptEntry<T extends Ingredient> {

	/**
	 * Weight in grams. Value >0;
	 */
	private Weight weight;

	/**
	 * Percentage of the {@link Ingredient}.
	 */
	private Percentage percentage;

	private T ingredient;

}
