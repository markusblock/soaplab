package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

public abstract class IngredientTableViewParent<T extends Ingredient> extends EntityTableView<T> {

	private static final long serialVersionUID = 1L;

	public IngredientTableViewParent(Class<T> entityClass, IngredientRepository<T> repository,
			boolean createEntityFunction) {
		super(entityClass, repository, createEntityFunction);

		addColumn(Ingredient.Fields.inci, "domain.ingredient.inci");
	}

	public IngredientTableViewParent(Class<T> entityClass, IngredientRepository<T> repository) {
		super(entityClass, repository, true);
	}
}
