package org.soaplab.domain;

import java.util.ArrayList;
import java.util.List;

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
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ExplicitEntity
@FieldNameConstants
public class LyeRecipe extends Recipe {

	private static final long serialVersionUID = 1L;

	/**
	 * NaOH and KOH in sum 100%
	 */
	private RecipeEntry<NaOH> naOH;

	/**
	 * NaOH and KOH in sum 100%
	 */
	private RecipeEntry<KOH> kOH;

	/**
	 * Optional notes on the recipt.
	 */
	private String notes;

	/**
	 * Calculated value. Lye costs.
	 */
	@Default
	private Price lyeCosts = Price.of(0);
	/**
	 * Calculated value. Total weight of NaOH.
	 */
	@Default
	private Weight naohWeight = Weight.ofGrams(0);
	/**
	 * Calculated value. Total weight of KOH.
	 */
	@Default
	private Weight kohWeight = Weight.ofGrams(0);

	/**
	 * Calculated value. Total weight of lye.
	 */
	@Default
	private Weight lyeWeight = Weight.ofGrams(0);

	private List<RecipeEntry<Liquid>> liquids = new ArrayList<>();
	/**
	 * Calculated value. Total weight of liquids.
	 */
	@Default
	private Weight liquidsWeight = Weight.ofGrams(0);
	/**
	 * Calculated value. Liquids costs.
	 */
	@Default
	private Price liquidsCosts = Price.of(0);

	@ToString.Exclude
	private List<RecipeEntry<Acid>> acids = new ArrayList<>();
	/**
	 * Calculated value. Total weight of acids.
	 */
	@Default
	private Weight acidsWeight = Weight.ofGrams(0);
	/**
	 * Calculated value. Acids costs.
	 */
	@Default
	private Price acidsCosts = Price.of(0);

	@ToString.Exclude
	private List<RecipeEntry<Additive>> additives = new ArrayList<>();
	/**
	 * Calculated value. Total weight of lye additives.
	 */
	@Default
	private Weight lyeAdditivesWeight = Weight.ofGrams(0);
	/**
	 * Calculated value. Lye additives costs.
	 */
	@Default
	private Price lyeAdditivesCosts = Price.of(0);

	@Override
	public LyeRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.acids(getRecipeEntryListDeepClone(acids)) //
				.liquids(getRecipeEntryListDeepClone(liquids)) //
				.additives(getRecipeEntryListDeepClone(additives)) //
				.naOH(naOH == null ? null : naOH.getCopyBuilder().build()) //
				.kOH(kOH == null ? null : kOH.getCopyBuilder().build());
	}
}
