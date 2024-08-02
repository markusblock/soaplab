package org.soaplab.ui.views.ingredient;

import org.apache.commons.lang3.NotImplementedException;
import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientsRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityTableView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "ingredient", layout = MainAppLayout.class)
public class IngredientView extends EntityTableView<Ingredient> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public IngredientView(IngredientsRepository repository) {
		super(Ingredient.class, repository, false);

		addColumn(Ingredient.Fields.inci, "domain.ingredient.inci");
		addPriceColumn(Ingredient.Fields.cost, "domain.ingredient.price");

	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.ingredients");
	}

	@Override
	protected Ingredient createNewEmptyEntity() {
		throw new NotImplementedException();
	}
}
