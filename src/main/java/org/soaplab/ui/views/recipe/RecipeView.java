package org.soaplab.ui.views.recipe;

import java.util.ArrayList;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.SoapRecipeRepository;
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
	private final FragranceRepository fragranceRepository;
	private final SoapCalculatorService soapCalculatorService;
	private final LyeRecipeRepository lyeRecipeRepository;
	private final AdditiveRepository additiveRepository;

	@Autowired
	public RecipeView(SoapRecipeRepository repository, LyeRecipeRepository lyeRecipeRepository,
			FatRepository fatRepository, FragranceRepository fragranceRepository, AdditiveRepository additiveRepository,
			SoapCalculatorService soapCalculatorService) {
		super(repository);
		this.lyeRecipeRepository = lyeRecipeRepository;
		this.fatRepository = fatRepository;
		this.fragranceRepository = fragranceRepository;
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
		return new RecipeDetailsPanel(callback, lyeRecipeRepository, fatRepository, fragranceRepository,
				additiveRepository, soapCalculatorService);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		// TODO move to builder
		final SoapRecipe soapRecipe = SoapRecipe.builder().build();
		soapRecipe.setFats(new ArrayList<RecipeEntry<Fat>>());
		soapRecipe.setFragrances(new ArrayList<RecipeEntry<Fragrance>>());
		soapRecipe.setAdditives(new ArrayList<RecipeEntry<Additive>>());
		return soapRecipe;
	}
}
