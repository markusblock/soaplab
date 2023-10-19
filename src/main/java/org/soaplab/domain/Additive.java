
package org.soaplab.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldNameConstants
public class Additive extends Ingredient {

	private static final long serialVersionUID = 1L;

	@Override
	public AdditiveBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder();
	}
}
