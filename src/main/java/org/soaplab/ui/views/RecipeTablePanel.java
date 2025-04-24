package org.soaplab.ui.views;

import org.soaplab.domain.Recipe;

public class RecipeTablePanel<T extends Recipe> extends NamedEntityTablePanel<T> {

	private static final long serialVersionUID = 1L;

	public RecipeTablePanel(Class<T> entityClass, EntityTableListener<T> listener) {
		super(entityClass, listener);

		addEntityColumn(Recipe.Fields.notes, "domain.recipe.notes");
	}
}
