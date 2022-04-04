package org.soaplab.ui.views;

public interface IngredientsViewDetailsControllerCallback<T> {

	void deleteIngredient(T ingredient);

	void editIngredient(T ingredient);

	void cancelEditMode();

	void createNewIngredient();

	void saveIngredient(T ingredient);
}