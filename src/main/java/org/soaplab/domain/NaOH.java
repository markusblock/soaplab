package org.soaplab.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
public class NaOH extends Lye {

	private static final long serialVersionUID = 1L;

}
