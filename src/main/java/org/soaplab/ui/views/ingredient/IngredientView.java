package org.soaplab.ui.views.ingredient;

import org.apache.commons.lang3.NotImplementedException;
import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "ingredient", layout = MainAppLayout.class)
public class IngredientView extends IngredientTableViewParent<Ingredient> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public IngredientView(IngredientRepository<Ingredient> repository) {
		super(Ingredient.class, repository, false);

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
