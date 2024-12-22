package org.soaplab.repository;

import java.util.List;

import org.soaplab.domain.Ingredient;

public interface AllIngredientsRepository extends EntityRepository<Ingredient> {

	List<Ingredient> findByInci(String inci);

	List<Ingredient> findByNameOrInci(String nameOrInci);

}
