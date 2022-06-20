package org.soaplab.ui.views.recipe;

import java.util.HashMap;
import java.util.UUID;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.FatRepository;
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
	private FatRepository fatRepository;

	@Autowired
	public RecipeView(SoapRecipeRepository repository, FatRepository fatRepository) {
		super(repository);
		this.fatRepository = fatRepository;
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
		return new RecipeDetailsPanel(callback, fatRepository);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		//TODO move to builder
		SoapRecipe soapRecipe = SoapRecipe.builder().build();
		soapRecipe.setFats(new HashMap<UUID, RecipeEntry<Fat>>());
		soapRecipe.setAcids(new HashMap<UUID, RecipeEntry<Acid>>());
		soapRecipe.setFragrances(new HashMap<UUID, RecipeEntry<Fragrance>>());
		soapRecipe.setLiquids(new HashMap<UUID, RecipeEntry<Liquid>>());
		return soapRecipe;
	}
}
