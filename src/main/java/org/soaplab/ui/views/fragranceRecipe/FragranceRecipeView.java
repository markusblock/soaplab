package org.soaplab.ui.views.fragranceRecipe;

import java.util.ArrayList;

import com.vaadin.flow.router.Route;
import org.soaplab.domain.*;
import org.soaplab.repository.*;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityList;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "fragrancerecipes", layout = MainAppLayout.class)
public class FragranceRecipeView extends EntityView<FragranceRecipe> {

	private static final long serialVersionUID = 1L;

	private final FragranceRepository fragranceRepository;

	@Autowired
	public FragranceRecipeView(FragranceRecipeRepository repository, FragranceRepository fragranceRepository) {
		super(repository);
		this.fragranceRepository = fragranceRepository;
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fragrancerecipes");
	}

	@Override
	protected EntityList<FragranceRecipe> createEntityList(EntityViewListControllerCallback<FragranceRecipe> callback) {
		return new EntityList<FragranceRecipe>(callback);
	}

	@Override
	protected FragranceRecipeDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<FragranceRecipe> callback) {
		return new FragranceRecipeDetailsPanel(callback, fragranceRepository);
	}

	@Override
	protected FragranceRecipe createNewEmptyEntity() {
		// TODO move to builder
		final FragranceRecipe fragranceRecipe = FragranceRecipe.builder().build();
		fragranceRecipe.setFragrances(new ArrayList<RecipeEntry<Fragrance>>());
		return fragranceRecipe;
	}
}
