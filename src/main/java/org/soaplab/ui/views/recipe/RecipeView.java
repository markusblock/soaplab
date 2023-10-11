package org.soaplab.ui.views.recipe;

import java.util.ArrayList;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.*;
import org.soaplab.service.soapcalc.SoapCalculatorService;
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

	private final FatRepository fatRepository;
	private final SoapCalculatorService soapCalculatorService;
	private final LyeRecipeRepository lyeRecipeRepository;
	private final FragranceRecipeRepository fragranceRecipeRepository;
	private final AdditiveRepository additiveRepository;

	@Autowired
	public RecipeView(SoapRecipeRepository repository, LyeRecipeRepository lyeRecipeRepository, FragranceRecipeRepository fragranceRecipeRepository,
			FatRepository fatRepository, AdditiveRepository additiveRepository,
			SoapCalculatorService soapCalculatorService) {
		super(repository);
		this.lyeRecipeRepository = lyeRecipeRepository;
		this.fatRepository = fatRepository;
		this.fragranceRecipeRepository = fragranceRecipeRepository;
		this.additiveRepository = additiveRepository;
		this.soapCalculatorService = soapCalculatorService;
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
		return new RecipeDetailsPanel(callback, lyeRecipeRepository, fragranceRecipeRepository, fatRepository,
				additiveRepository, soapCalculatorService);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		// TODO move to builder
		final SoapRecipe soapRecipe = SoapRecipe.builder().build();
		soapRecipe.setFats(new ArrayList<RecipeEntry<Fat>>());
		soapRecipe.setAdditives(new ArrayList<RecipeEntry<Additive>>());
		return soapRecipe;
	}
}
