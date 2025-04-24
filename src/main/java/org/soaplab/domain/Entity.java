package org.soaplab.domain;

import java.beans.Transient;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldNameConstants
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Builder.Default
	@EqualsAndHashCode.Include
	private UUID id = UUID.randomUUID();

	/*
	 * Version 0 means not yet stored. First stored version will be 1;
	 */
	private Long version = Long.valueOf(0);

	@JsonIgnore
	@Transient
	public abstract EntityBuilder<?, ?> getCopyBuilder();

}
