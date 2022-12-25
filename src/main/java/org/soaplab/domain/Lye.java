package org.soaplab.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@SuperBuilder(toBuilder = true)
public class Lye extends Ingredient {

	private static final long serialVersionUID = 1L;

}
