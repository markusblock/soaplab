package org.soaplab.ui.views;

public interface IngredientsViewControllerCallback<T> {

	void onSaveIngredient(T ingredient);

	void onCreateNewIngredient();

}