package org.soaplab.ui.views.ingredient;

import org.apache.commons.lang3.NotImplementedException;
import org.soaplab.domain.Ingredient;
import org.soaplab.repository.AllIngredientsRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityDetailsPanel;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityTablePanel;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientDetails;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "ingredients", layout = MainAppLayout.class)
public class AllIngredientsView extends EntityView<Ingredient> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AllIngredientsView(AllIngredientsRepository repository, FatRepository fatRepository) {
		super(repository, "domain.ingredients");
	}

	@Override
	protected EntityTablePanel<Ingredient> createEntityTable(EntityTableListener<Ingredient> listener) {
		return new AllIngredientsTablePanel(listener);
	}

	@Override
	protected EntityDetailsPanel<Ingredient> createEntityDetails(
			EntityDetailsListener<Ingredient> entityDetailsListener) {
		return new IngredientDetails<>(entityDetailsListener);
	}

	@Override
	protected Ingredient createNewEmptyEntity() {
		throw new NotImplementedException();
	}

	@Override
	protected boolean isCreateNewEntityAllowed() {
		return false;
	}
}
