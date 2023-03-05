package org.soaplab.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an entity as ImplicitEntity which means it is used in
 * compositions but can't live on its own. Can be used e.g. in storage strategy
 * to store an implicit entity always together with its containing
 * ExplicitEntity.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImplicitEntity {
	// marker interface
}
