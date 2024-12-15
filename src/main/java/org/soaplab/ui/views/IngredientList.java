package org.soaplab.ui.views;

import java.util.List;

import org.soaplab.domain.Ingredient;

public class IngredientList<T extends Ingredient> extends EntityList<T> {

	private static final long serialVersionUID = 1L;

	public IngredientList(EntityTableListener<T> callback) {
		super(callback);

		// neccessary for cast later
//		Assert.isAssignable(IngredientRepository.class, callback.getRepository().getClass());

		getEntityGrid().addColumn(Ingredient::getInci).setHeader(getTranslation("domain.ingredient.inci"))
				.setSortable(true);
	}

	protected List<T> getFilteredEntities(String searchString) {
//		return ((IngredientRepository<T>) getCallback().getRepository()).findByNameOrInci(searchString);
		return null;
	}
}
