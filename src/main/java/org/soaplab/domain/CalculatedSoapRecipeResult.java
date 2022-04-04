package org.soaplab.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CalculatedSoapRecipeResult extends NamedEntity {

	private static final long serialVersionUID = 1L;

	private Weight naohTotal;
	private Weight kohTotal;
	private Weight liquidTotal;
	private Weight weightTotal;
	private Price priceTotal;

	private Map<UUID, CalculatedRecipeEntry<Fat>> fats = new HashMap<>();
	private Map<UUID, CalculatedRecipeEntry<Acid>> acids = new HashMap<>();
	private Map<UUID, CalculatedRecipeEntry<Fragrance>> fragrances = new HashMap<>();
	private Map<UUID, CalculatedRecipeEntry<Liquid>> liquids = new HashMap<>();

	@Override
	public CalculatedSoapRecipeResult getClone() {
		return new CalculatedSoapRecipeResult(this.toBuilder());
	}

}
