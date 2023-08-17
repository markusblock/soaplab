package org.soaplab.ui.views.lyerecipe;

import java.util.List;

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
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.RecipeEntryList;
import org.springframework.util.CollectionUtils;

public class LyeRecipeDetailsPanel extends EntityDetails<LyeRecipe> {

	private static final long serialVersionUID = 1L;
	private final RecipeEntryList<Acid> acids;
	private final RecipeEntryList<Additive> additives;
	private final RecipeEntryList<Liquid> liquids;
	private final RecipeEntryList<NaOH> naOH;
	private final RecipeEntryList<KOH> kOH;

	private LyeRecipe lyeRecipe;

	public LyeRecipeDetailsPanel(EntityViewDetailsControllerCallback<LyeRecipe> callback, AcidRepository acidRepository,
			LiquidRepository liquidRepository, NaOHRepository naOHRepository, KOHRepository kOHRepository,
			AdditiveRepository additiveRepository) {
		super(callback);

		addPropertyTextArea("domain.recipe.notes", LyeRecipe::getNotes, LyeRecipe::setNotes);

		naOH = new RecipeEntryList<NaOH>(naOHRepository, "domain.naoh");
		naOH.setWidthFull();
		addContent(naOH);

		kOH = new RecipeEntryList<KOH>(kOHRepository, "domain.koh");
		kOH.setWidthFull();
		addContent(kOH);

		acids = new RecipeEntryList<>(acidRepository, "domain.acids");
		acids.setWidthFull();
		addContent(acids);

		liquids = new RecipeEntryList<>(liquidRepository, "domain.liquids");
		liquids.setWidthFull();
		addContent(liquids);

		additives = new RecipeEntryList<>(additiveRepository, "domain.additives");
		additives.setWidthFull();
		addContent(additives);

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
	protected void preSave() {
		super.preSave();
		lyeRecipe.setAcids(List.copyOf(acids.getData()));
		lyeRecipe.setLiquids(List.copyOf(liquids.getData()));
		lyeRecipe.setAdditives(List.copyOf(additives.getData()));
		lyeRecipe.setNaOH(CollectionUtils.firstElement(naOH.getData()));
		lyeRecipe.setKOH(CollectionUtils.firstElement(kOH.getData()));
	}

	@Override
	protected void setEntity(LyeRecipe entity) {
		this.lyeRecipe = entity;

		if (lyeRecipe == null) {
			naOH.setData();
			kOH.setData();
			acids.setData();
			liquids.setData();
			additives.setData();
		} else {
			if (lyeRecipe.getNaOH() == null) {
				naOH.setData();
			} else {
				naOH.setData(List.of(lyeRecipe.getNaOH()));
			}
			if (lyeRecipe.getKOH() == null) {
				kOH.setData();
			} else {
				kOH.setData(List.of(lyeRecipe.getKOH()));
			}
			acids.setData(lyeRecipe.getAcids());
			liquids.setData(lyeRecipe.getLiquids());
			additives.setData(lyeRecipe.getAdditives());
		}
	}
}
