package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

public interface IngredientsViewListControllerCallback<T extends Ingredient> {

	void ingredientSelected(T ingredient);

	IngredientRepository<T> getRepository();

}