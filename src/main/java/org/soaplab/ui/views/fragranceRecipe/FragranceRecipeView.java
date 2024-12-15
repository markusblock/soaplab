package org.soaplab.ui.views.fragranceRecipe;

import java.util.ArrayList;

import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceRecipe;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.repository.FragranceRecipeRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityTablePanel;
import org.soaplab.ui.views.EntityView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fragrancerecipes", layout = MainAppLayout.class)
public class FragranceRecipeView extends EntityView<FragranceRecipe> {

	private static final long serialVersionUID = 1L;

	private final FragranceRecipeRepository fragranceRecipeRepository;
	private final FragranceRepository fragranceRepository;

	@Autowired
	public FragranceRecipeView(FragranceRecipeRepository fragranceRecipeRepository,
			FragranceRepository fragranceRepository) {
		super(fragranceRecipeRepository, "domain.fragrancerecipes");
		this.fragranceRecipeRepository = fragranceRecipeRepository;
		this.fragranceRepository = fragranceRepository;
	}

	@Override
	protected EntityTablePanel<FragranceRecipe> createEntityTable(EntityTableListener<FragranceRecipe> listener) {
		return new EntityTablePanel<FragranceRecipe>(FragranceRecipe.class, fragranceRecipeRepository, listener);
	}

	@Override
	protected FragranceRecipeDetailsPanel createEntityDetails(EntityDetailsListener<FragranceRecipe> listener) {
		return new FragranceRecipeDetailsPanel(listener, fragranceRepository);
	}

	@Override
	protected FragranceRecipe createNewEmptyEntity() {
		// TODO move to builder
		final FragranceRecipe fragranceRecipe = FragranceRecipe.builder().build();
		fragranceRecipe.setFragrances(new ArrayList<RecipeEntry<Fragrance>>());
		return fragranceRecipe;
	}
}
