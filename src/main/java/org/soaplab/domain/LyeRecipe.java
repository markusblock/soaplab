package org.soaplab.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.soaplab.domain.utils.SoapRecipeUtils;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ExplicitEntity
@FieldNameConstants
public class LyeRecipe extends NamedEntity {

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
	private Price lyeCosts;
	/**
	 * Calculated value. Total weight of NaOH.
	 */
	private Weight naohTotal;
	/**
	 * Calculated value. Total weight of KOH.
	 */
	private Weight kohTotal;

	/**
	 * Calculated value. Total weight of lye.
	 */
	private Weight lyeTotal;

	private List<RecipeEntry<Liquid>> liquids = new ArrayList<>();
	/**
	 * Calculated value. Total weight of liquids.
	 */
	private Weight liquidsTotal;
	/**
	 * Calculated value. Liquids costs.
	 */
	private Price liquidsCosts;

	private List<RecipeEntry<Acid>> acids = new ArrayList<>();
	/**
	 * Calculated value. Total weight of acids.
	 */
	private Weight acidsTotal;
	/**
	 * Calculated value. Acids costs.
	 */
	private Price acidsCosts;

	private List<RecipeEntry<Additive>> additives = new ArrayList<>();
	/**
	 * Calculated value. Total weight of lye additives.
	 */
	private Weight lyeAdditivesTotal;
	/**
	 * Calculated value. Lye additives costs.
	 */
	private Price lyeAdditivesCosts;

	@Override
	public LyeRecipeBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder() //
				.acids(SoapRecipeUtils.getRecipeEntryListDeepClone(acids)) //
				.liquids(SoapRecipeUtils.getRecipeEntryListDeepClone(liquids)) //
				.additives(SoapRecipeUtils.getRecipeEntryListDeepClone(additives)) //
				.naOH(naOH == null ? null : naOH.getCopyBuilder().build()) //
				.kOH(kOH == null ? null : kOH.getCopyBuilder().build());
	}
}
