
package org.soaplab.domain;

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
@FieldNameConstants
public class NamedEntity extends Entity {

	private static final long serialVersionUID = 1L;

	private String name;

	@Override
	public NamedEntityBuilder<?, ?> getCopyBuilder() {
		return this.toBuilder();
	}
}
