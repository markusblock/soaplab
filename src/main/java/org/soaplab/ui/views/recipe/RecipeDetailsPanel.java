package org.soaplab.ui.views.recipe;

import java.util.List;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRecipeRepository;
import org.soaplab.repository.LyeRecipeRepository;
import org.soaplab.service.soapcalc.SoapCalculatorService;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.RecipeEntryList;

public class RecipeDetailsPanel extends EntityDetails<SoapRecipe> {

    private static final long serialVersionUID = 1L;
    private final RecipeEntryList<Fat> fats;
    private final RecipeEntryList<Additive> additives;
    private SoapRecipe soapRecipe;
    private final SoapCalculatorService soapCalculatorService;

    public RecipeDetailsPanel(EntityViewDetailsControllerCallback<SoapRecipe> callback,
            LyeRecipeRepository lyeRecipeRepository, FragranceRecipeRepository fragranceRecipeRepository,
            FatRepository fatRepository, AdditiveRepository additiveRepository,
            SoapCalculatorService soapCalculatorService) {
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

        addEntitySelector("domain.lyerecipe", SoapRecipe::getLyeRecipe, SoapRecipe::setLyeRecipe, lyeRecipeRepository);

        addEntitySelector("domain.fragrancerecipe", SoapRecipe::getFragranceRecipe, SoapRecipe::setFragranceRecipe,
                fragranceRecipeRepository);

        fats = new RecipeEntryList<Fat>(fatRepository, "domain.fats");
        // TODO add panel the same way as the propertypanels
        fats.setWidthFull();
        addContent(fats);

        additives = new RecipeEntryList<>(additiveRepository, "domain.additives");
        additives.setWidthFull();
        addContent(additives);

    }

    @Override
    protected void enterEditMode() {
        super.enterEditMode();
        fats.enterEditMode();
        additives.enterEditMode();
    }

    @Override
    protected void leaveEditMode() {
        super.leaveEditMode();
        fats.leaveEditMode();
        additives.leaveEditMode();
    }

    @Override
    protected void preSave() {
        super.preSave();
        soapRecipe.setFats(List.copyOf(fats.getData()));
        soapRecipe.setAdditives(List.copyOf(additives.getData()));
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
            fats.setData();
            additives.setData();
        } else {
            fats.setData(soapRecipe.getFats());
            additives.setData(soapRecipe.getAdditives());
        }
    }
}
