package org.soaplab.domain;

import java.beans.Transient;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldNameConstants
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	// TODO move id creation out of repo into service layer, detect a new entity by
	// empty version attribute
	private UUID id;

	@JsonIgnore
	@Transient
	public abstract EntityBuilder<?, ?> getCopyBuilder();

}
