package org.soaplab.ui.views.lyerecipe;

import java.util.List;
import java.util.Optional;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Additive;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.LyeRecipe;
import org.soaplab.domain.NaOH;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityDetailsPanel;
import org.soaplab.ui.views.RecipeEntryTable;
import org.springframework.util.CollectionUtils;

public class LyeRecipeDetailsPanel extends EntityDetailsPanel<LyeRecipe> {

	private static final long serialVersionUID = 1L;
	private final RecipeEntryTable<Acid> acids;
	private final RecipeEntryTable<Additive> additives;
	private final RecipeEntryTable<Liquid> liquids;
	private final RecipeEntryTable<NaOH> naOH;
	private final RecipeEntryTable<KOH> kOH;

	private Optional<LyeRecipe> lyeRecipe;

	public LyeRecipeDetailsPanel(EntityDetailsListener<LyeRecipe> callback, AcidRepository acidRepository,
			LiquidRepository liquidRepository, NaOHRepository naOHRepository, KOHRepository kOHRepository,
			AdditiveRepository additiveRepository) {
		super(callback);

		addPropertyTextArea("domain.recipe.notes", LyeRecipe::getNotes, LyeRecipe::setNotes);

		naOH = new RecipeEntryTable<NaOH>(naOHRepository, "domain.naoh");
		naOH.setWidthFull();
		addRecipeEntryTable(naOH);

		kOH = new RecipeEntryTable<KOH>(kOHRepository, "domain.koh");
		kOH.setWidthFull();
		addRecipeEntryTable(kOH);

		acids = new RecipeEntryTable<>(acidRepository, "domain.acids");
		acids.setWidthFull();
		addRecipeEntryTable(acids);

		liquids = new RecipeEntryTable<>(liquidRepository, "domain.liquids");
		liquids.setWidthFull();
		addRecipeEntryTable(liquids);

		additives = new RecipeEntryTable<>(additiveRepository, "domain.additives");
		additives.setWidthFull();
		addRecipeEntryTable(additives);

	}

	@Override
	protected void enterEditMode() {
		super.enterEditMode();
		naOH.enterEditMode();
		kOH.enterEditMode();
		acids.enterEditMode();
		liquids.enterEditMode();
		additives.enterEditMode();
	}

	@Override
	protected void leaveEditMode() {
		super.leaveEditMode();
		naOH.leaveEditMode();
		kOH.leaveEditMode();
		acids.leaveEditMode();
		liquids.leaveEditMode();
		additives.leaveEditMode();
	}

	@Override
	protected void updateEntityWithChangesFromUI() {
		super.updateEntityWithChangesFromUI();
		lyeRecipe.orElseThrow().setAcids(List.copyOf(acids.getEntities()));
		lyeRecipe.orElseThrow().setLiquids(List.copyOf(liquids.getEntities()));
		lyeRecipe.orElseThrow().setAdditives(List.copyOf(additives.getEntities()));
		lyeRecipe.orElseThrow().setNaOH(CollectionUtils.firstElement(naOH.getEntities()));
		lyeRecipe.orElseThrow().setKOH(CollectionUtils.firstElement(kOH.getEntities()));
	}

	@Override
	protected void setEntity(Optional<LyeRecipe> entity) {
		this.lyeRecipe = entity;

		entity.ifPresentOrElse(t -> {
			if (t.getNaOH() == null) {
				naOH.setEntities();
			} else {
				naOH.setEntities(List.of(t.getNaOH()));
			}
			if (t.getKOH() == null) {
				kOH.setEntities();
			} else {
				kOH.setEntities(List.of(t.getKOH()));
			}
			acids.setEntities(t.getAcids());
			liquids.setEntities(t.getLiquids());
			additives.setEntities(t.getAdditives());
		}, () -> {
			naOH.setEntities();
			kOH.setEntities();
			acids.setEntities();
			liquids.setEntities();
			additives.setEntities();
		});
	}
}
