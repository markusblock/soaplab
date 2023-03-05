package org.soaplab.repository.microstream;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.soaplab.domain.ExplicitEntity;
import org.soaplab.domain.ImplicitEntity;

import one.microstream.persistence.types.PersistenceEagerStoringFieldEvaluator;

/**
 * Custom field evaluator which looks for the {@link ImplicitEntity} annotation
 * on the referenced class. The reference should be stored together with the
 * containing {@link ExplicitEntity}.
 *
 */
public class StoreEagerEvaluator implements PersistenceEagerStoringFieldEvaluator {

	@Override
	public boolean isEagerStoring(final Class<?> clazz, final Field field) {

		boolean annotationPresentOnFieldClass = false;

		// navigating to the referenced class and check if the implicitEntity annotation
		// is present, e.g. SoapRecipe.naOH => RecipeEntry, has ImplicitEntity
		// annotation
		annotationPresentOnFieldClass = field.getType().isAnnotationPresent(ImplicitEntity.class);

		// for a field like SoapRecipe.fats the type is set as generic type to the list.
		// we need to navigate to this generic type and check the annotations
		if (!annotationPresentOnFieldClass && field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType genericType = (ParameterizedType) field.getGenericType();
			if (field.getGenericType() instanceof ParameterizedType) {
				if (genericType.getActualTypeArguments().length > 0) {
					if (genericType.getActualTypeArguments()[0] instanceof ParameterizedType) {
						ParameterizedType type = (ParameterizedType) genericType.getActualTypeArguments()[0];
						Class<?> rawType = (Class<?>) type.getRawType();
						annotationPresentOnFieldClass = rawType.isAnnotationPresent(ImplicitEntity.class);
					}
				}
			}
		}

		return annotationPresentOnFieldClass;
	}
}
