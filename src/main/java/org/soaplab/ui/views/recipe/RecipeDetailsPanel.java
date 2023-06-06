package org.soaplab.ui.views.recipe;

import java.util.List;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.NaOH;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.KOHRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.service.SoapCalculatorService;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.springframework.util.CollectionUtils;

public class RecipeDetailsPanel extends EntityDetails<SoapRecipe> {

	private static final long serialVersionUID = 1L;
	private final RecipeEntryList<Fat> fats;
	private final RecipeEntryList<Acid> acids;
	private final RecipeEntryList<Liquid> liquids;
	private final RecipeEntryList<Fragrance> fragrances;
	private final RecipeEntryList<NaOH> naOH;
	private final RecipeEntryList<KOH> kOH;
	private SoapRecipe soapRecipe;
	private final SoapCalculatorService soapCalculatorService;

	public RecipeDetailsPanel(EntityViewDetailsControllerCallback<SoapRecipe> callback, FatRepository fatRepository,
			AcidRepository acidRepository, LiquidRepository liquidRepository, FragranceRepository fragranceRepository,
			NaOHRepository naOHRepository, KOHRepository kOHRepository, SoapCalculatorService soapCalculatorService) {
		super(callback);
		this.soapCalculatorService = soapCalculatorService;
		addPropertyWeightField("domain.recipe.fatstotal", SoapRecipe::getFatsTotal, SoapRecipe::setFatsTotal);
		addPropertyPercentageField("domain.recipe.superfat", SoapRecipe::getSuperFat, SoapRecipe::setSuperFat);
		addPropertyPercentageField("domain.recipe.liquidtofatratio", SoapRecipe::getLiquidToFatRatio,
				SoapRecipe::setLiquidToFatRatio);
		addPropertyPercentageField("domain.recipe.fragrancetotal", SoapRecipe::getFragranceToFatRatio,
				SoapRecipe::setFragranceToFatRatio);
		addPropertyTextArea("domain.recipe.notes", SoapRecipe::getNotes, SoapRecipe::setNotes);

		addPropertyWeightFieldReadOnly("domain.recipe.naohTotal", recipe -> recipe.getLyeRecipe().getNaohTotal());
		addPropertyWeightFieldReadOnly("domain.recipe.kohTotal", recipe -> recipe.getLyeRecipe().getKohTotal());
		addPropertyWeightFieldReadOnly("domain.recipe.liquidTotal", recipe -> recipe.getLyeRecipe().getLiquidsTotal());
		addPropertyWeightFieldReadOnly("domain.recipe.weightTotal", SoapRecipe::getWeightTotal);
		addPropertyPriceFieldReadOnly("domain.recipe.costsTotal", SoapRecipe::getCostsTotal);

		naOH = new RecipeEntryList<NaOH>(naOHRepository, "domain.naoh");
		naOH.setWidthFull();
		addContent(naOH);

		kOH = new RecipeEntryList<KOH>(kOHRepository, "domain.koh");
		kOH.setWidthFull();
		addContent(kOH);

		fats = new RecipeEntryList<Fat>(fatRepository, "domain.fats");
		// TODO add panel the same way as the propertypanels
		fats.setWidthFull();
		addContent(fats);

		acids = new RecipeEntryList<>(acidRepository, "domain.acids");
		acids.setWidthFull();
		addContent(acids);

		liquids = new RecipeEntryList<>(liquidRepository, "domain.liquids");
		liquids.setWidthFull();
		addContent(liquids);

		fragrances = new RecipeEntryList<>(fragranceRepository, "domain.fragrances");
		fragrances.setWidthFull();
		addContent(fragrances);

	}

	@Override
	protected void enterEditMode() {
		super.enterEditMode();
		naOH.enterEditMode();
		kOH.enterEditMode();
		fats.enterEditMode();
		acids.enterEditMode();
		liquids.enterEditMode();
		fragrances.enterEditMode();
	}

	@Override
	protected void leaveEditMode() {
		super.leaveEditMode();
		naOH.leaveEditMode();
		kOH.leaveEditMode();
		fats.leaveEditMode();
		acids.leaveEditMode();
		liquids.leaveEditMode();
		fragrances.leaveEditMode();
	}

	@Override
	protected void preSave() {
		super.preSave();
		soapRecipe.getLyeRecipe().setNaOH(CollectionUtils.firstElement(naOH.getData()));
		soapRecipe.getLyeRecipe().setKOH(CollectionUtils.firstElement(kOH.getData()));
		soapRecipe.setFats(List.copyOf(fats.getData()));
		soapRecipe.getLyeRecipe().setAcids(List.copyOf(acids.getData()));
		soapRecipe.getLyeRecipe().setLiquids(List.copyOf(liquids.getData()));
		soapRecipe.setFragrances(List.copyOf(fragrances.getData()));
	}

	@Override
	protected void processEntity(SoapRecipe entity) {
		super.processEntity(entity);

		soapCalculatorService.calculate(entity);
	}

	@Override
	protected void setEntity(SoapRecipe soapRecipe) {
		this.soapRecipe = soapRecipe;

		if (soapRecipe == null) {
			naOH.setData();
			kOH.setData();
			fats.setData();
			acids.setData();
			liquids.setData();
			fragrances.setData();
		} else {
			if (soapRecipe.getLyeRecipe().getNaOH() == null) {
				naOH.setData();
			} else {
				naOH.setData(List.of(soapRecipe.getLyeRecipe().getNaOH()));
			}
			if (soapRecipe.getLyeRecipe().getKOH() == null) {
				kOH.setData();
			} else {
				kOH.setData(List.of(soapRecipe.getLyeRecipe().getKOH()));
			}
			fats.setData(soapRecipe.getFats());
			acids.setData(soapRecipe.getLyeRecipe().getAcids());
			liquids.setData(soapRecipe.getLyeRecipe().getLiquids());
			fragrances.setData(soapRecipe.getFragrances());
		}
	}
}
