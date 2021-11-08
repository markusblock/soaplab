package org.soaplab.repository;

import java.util.List;

import org.soaplab.domain.Ingredient;

public interface IngredientRepository<T extends Ingredient> extends EntityRepository<T> {

	List<T> findByInci(String inci);

}
