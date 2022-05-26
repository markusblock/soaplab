package org.soaplab.ui.views.recipe;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.SoapRecipeRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityList;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "recipes", layout = MainAppLayout.class)
public class RecipeView extends EntityView<SoapRecipe> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public RecipeView(SoapRecipeRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.recipes");
	}

	@Override
	protected EntityList<SoapRecipe> createEntityList(EntityViewListControllerCallback<SoapRecipe> callback) {
		return new EntityList<SoapRecipe>(callback);
	}

	@Override
	protected RecipeDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<SoapRecipe> callback) {
		return new RecipeDetailsPanel(callback);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		return SoapRecipe.builder().build();
	}
}
