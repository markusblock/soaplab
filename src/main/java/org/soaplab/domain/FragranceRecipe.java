/*
 * Copyright (c) 2023. Bosch.IO GmbH
 */
package org.soaplab.domain;

import java.util.ArrayList;
import java.util.List;

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
@ExplicitEntity
@FieldNameConstants
public class FragranceRecipe extends Recipe {
	private static final long serialVersionUID = 1L;

	@ToString.Exclude
	private List<RecipeEntry<Fragrance>> fragrances = new ArrayList<>();

	@Override
	public FragranceRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.fragrances(getRecipeEntryListDeepClone(fragrances));
	}

	public RecipeEntry<Fragrance> getRecipeEntry(Fragrance fragrance) {
		return getRecipeEntry(fragrance, getFragrances());
	}
}
