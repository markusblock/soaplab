package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

public class IngredientTablePanel<T extends Ingredient> extends EntityTablePanel<T> {

	private static final long serialVersionUID = 1L;

	public IngredientTablePanel(Class<T> entityClass, IngredientRepository<T> repository,
			EntityTableListener<T> listener) {
		super(entityClass, repository, listener);

		addColumn(Ingredient.Fields.inci, "domain.ingredient.inci");
	}
}
