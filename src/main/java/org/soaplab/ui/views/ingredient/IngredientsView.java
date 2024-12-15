package org.soaplab.ui.views.ingredient;

import org.apache.commons.lang3.NotImplementedException;
import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityDetailsPanel;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityTablePanel;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "ingredients", layout = MainAppLayout.class)
public class IngredientsView extends EntityView<Ingredient> {

	private static final long serialVersionUID = 1L;

	private IngredientRepository<Ingredient> repository;

	@Autowired
	public IngredientsView(IngredientRepository<Ingredient> repository) {
		super(repository, "domain.ingredients");
		this.repository = repository;
	}

	@Override
	protected EntityTablePanel<Ingredient> createEntityTable(EntityTableListener<Ingredient> listener) {
		return new IngredientTablePanel<Ingredient>(Ingredient.class, repository, listener);
	}

	@Override
	protected EntityDetailsPanel<Ingredient> createEntityDetails(EntityDetailsListener<Ingredient> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Ingredient createNewEmptyEntity() {
		throw new NotImplementedException();
	}

}
