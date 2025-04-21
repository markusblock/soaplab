package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

public class IngredientTablePanel<T extends Ingredient> extends NamedEntityTablePanel<T> {

	private static final long serialVersionUID = 1L;

	public IngredientTablePanel(Class<T> entityClass, EntityTableListener<T> listener) {
		super(entityClass, listener);

		addEntityColumn(Ingredient.Fields.inci, "domain.ingredient.inci");
	}
}
