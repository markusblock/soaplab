package org.soaplab.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark an entity as {@link ExplicitEntity} which means it can
 * live no its own and should also be stored separately when changed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExplicitEntity {
	// marker interface
}
