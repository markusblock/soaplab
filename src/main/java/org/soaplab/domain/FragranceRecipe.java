/*
 * Copyright (c) 2023. Bosch.IO GmbH
 */
package org.soaplab.domain;

import java.util.ArrayList;
import java.util.List;

import org.soaplab.domain.utils.SoapRecipeUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ExplicitEntity
@FieldNameConstants
public class FragranceRecipe extends NamedEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * Optional notes on the recipt.
	 */
	private String notes;

	/**
	 * Calculated value. Recipe costs.
	 */
	private Price costs;

	/**
	 * Calculated value. Total weight of recipe.
	 */
	private Weight weight;

	private List<RecipeEntry<Fragrance>> fragrances = new ArrayList<>();

	@Override
	public FragranceRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.fragrances(SoapRecipeUtils.getRecipeEntryListDeepClone(fragrances));
	}

	public RecipeEntry<Fragrance> getRecipeEntry(Fragrance fragrance) {
		return fragrances.stream().filter(recipeEntry -> recipeEntry.getIngredient().equals(fragrance)).findAny()
				.orElseThrow();
	}
}
