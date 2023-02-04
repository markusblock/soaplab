package org.soaplab.ui.views.recipe;

import java.util.ArrayList;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.repository.SoapRecipeRepository;
import org.soaplab.service.SoapCalculatorService;
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
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final FragranceRepository fragranceRepository;
	private final SoapCalculatorService soapCalculatorService;
	private final NaOHRepository naOHRepository;
	private final KOHRepository kOHRepository;

	@Autowired
	public RecipeView(SoapRecipeRepository repository, FatRepository fatRepository, AcidRepository acidRepository,
			LiquidRepository liquidRepository, FragranceRepository fragranceRepository, NaOHRepository naOHRepository,
			KOHRepository kOHRepository, SoapCalculatorService soapCalculatorService) {
		super(repository);
		this.fatRepository = fatRepository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
		this.naOHRepository = naOHRepository;
		this.kOHRepository = kOHRepository;
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
		return new RecipeDetailsPanel(callback, fatRepository, acidRepository, liquidRepository, fragranceRepository,
				naOHRepository, kOHRepository, soapCalculatorService);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		// TODO move to builder
		final SoapRecipe soapRecipe = SoapRecipe.builder().build();
		soapRecipe.setFats(new ArrayList<RecipeEntry<Fat>>());
		soapRecipe.setAcids(new ArrayList<RecipeEntry<Acid>>());
		soapRecipe.setFragrances(new ArrayList<RecipeEntry<Fragrance>>());
		soapRecipe.setLiquids(new ArrayList<RecipeEntry<Liquid>>());
		return soapRecipe;
	}
}
