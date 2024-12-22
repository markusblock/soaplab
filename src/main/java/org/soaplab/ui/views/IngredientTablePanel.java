package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

public class IngredientTablePanel<T extends Ingredient> extends EntityTablePanel<T> {

	private static final long serialVersionUID = 1L;

	public IngredientTablePanel(Class<T> entityClass, EntityTableListener<T> listener) {
		super(entityClass, listener);

		addColumn(Ingredient.Fields.inci, "domain.ingredient.inci");
	}
}
