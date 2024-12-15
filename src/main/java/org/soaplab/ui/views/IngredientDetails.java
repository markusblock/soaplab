package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

public abstract class IngredientDetails<T extends Ingredient> extends EntityDetailsPanel<T> {

	private static final long serialVersionUID = 1L;

	protected IngredientDetails(EntityDetailsListener<T> callback) {
		super(callback);

		addPropertyStringField("domain.ingredient.inci", Ingredient::getInci, Ingredient::setInci, false);
		addPropertyPriceField("domain.ingredient.price", Ingredient::getCost, Ingredient::setCost);

	}
}
