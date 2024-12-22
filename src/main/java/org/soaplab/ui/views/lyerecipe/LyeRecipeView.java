package org.soaplab.ui.views.lyerecipe;

import java.util.ArrayList;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityTablePanel;
import org.soaplab.ui.views.EntityView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "lyerecipes", layout = MainAppLayout.class)
public class LyeRecipeView extends EntityView<LyeRecipe> {

	private static final long serialVersionUID = 1L;

	private final LyeRecipeRepository repository;
	private final AcidRepository acidRepository;
	private final LiquidRepository liquidRepository;
	private final NaOHRepository naOHRepository;
	private final KOHRepository kOHRepository;
	private final AdditiveRepository additiveRepository;

	@Autowired
	public LyeRecipeView(LyeRecipeRepository repository, AcidRepository acidRepository,
			LiquidRepository liquidRepository, NaOHRepository naOHRepository, KOHRepository kOHRepository,
			AdditiveRepository additiveRepository) {
		super(repository, "domain.lyerecipes");
		this.repository = repository;
		this.acidRepository = acidRepository;
		this.liquidRepository = liquidRepository;
		this.naOHRepository = naOHRepository;
		this.kOHRepository = kOHRepository;
		this.additiveRepository = additiveRepository;
	}

	@Override
	protected EntityTablePanel<LyeRecipe> createEntityTable(EntityTableListener<LyeRecipe> listener) {
		return new EntityTablePanel<LyeRecipe>(LyeRecipe.class, listener);
	}

	@Override
	protected LyeRecipeDetailsPanel createEntityDetails(EntityDetailsListener<LyeRecipe> listener) {
		return new LyeRecipeDetailsPanel(listener, acidRepository, liquidRepository, naOHRepository, kOHRepository,
				additiveRepository);
	}

	@Override
	protected LyeRecipe createNewEmptyEntity() {
		// TODO move to builder
		final LyeRecipe lyeRecipe = LyeRecipe.builder().build();
		lyeRecipe.setAcids(new ArrayList<RecipeEntry<Acid>>());
		lyeRecipe.setLiquids(new ArrayList<RecipeEntry<Liquid>>());
		lyeRecipe.setAdditives(new ArrayList<RecipeEntry<Additive>>());
		return lyeRecipe;
	}
}
