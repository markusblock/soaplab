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
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Fragrance extends Ingredient {

	private static final long serialVersionUID = 1L;

	private FragranceType type;

	@Override
	public Fragrance getClone() {
		return new Fragrance(this.toBuilder());
	}
}
