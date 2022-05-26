package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

public abstract class IngredientDetails<T extends Ingredient> extends EntityDetails<T> {

	private static final long serialVersionUID = 1L;

	protected IngredientDetails(EntityViewDetailsControllerCallback<T> callback) {
		super(callback);

		addPropertyStringField("domain.ingredient.inci", Ingredient::getInci, Ingredient::setInci, false);

	}
}
