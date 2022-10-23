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
import org.soaplab.repository.LiquidRepository;
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
	private FatRepository fatRepository;
	private AcidRepository acidRepository;
	private LiquidRepository liquidRepository;
	private FragranceRepository fragranceRepository;
	private SoapCalculatorService soapCalculatorService;

	@Autowired
	public RecipeView(SoapRecipeRepository repository, FatRepository fatRepository, AcidRepository acidRepository,
			LiquidRepository liquidRepository, FragranceRepository fragranceRepository,
			SoapCalculatorService soapCalculatorService) {
		super(repository);
		this.fatRepository = fatRepository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.fragranceRepository = fragranceRepository;
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
				soapCalculatorService);
	}

	@Override
	protected SoapRecipe createNewEmptyEntity() {
		// TODO move to builder
		SoapRecipe soapRecipe = SoapRecipe.builder().build();
		soapRecipe.setFats(new ArrayList<RecipeEntry<Fat>>());
		soapRecipe.setAcids(new ArrayList<RecipeEntry<Acid>>());
		soapRecipe.setFragrances(new ArrayList<RecipeEntry<Fragrance>>());
		soapRecipe.setLiquids(new ArrayList<RecipeEntry<Liquid>>());
		return soapRecipe;
	}
}
