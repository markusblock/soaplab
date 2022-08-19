package org.soaplab.ui.views.recipe;

import java.util.List;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.domain.Fragrance;
import org.soaplab.domain.Liquid;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.AcidRepository;
import org.soaplab.repository.FatRepository;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

public class RecipeDetailsPanel extends EntityDetails<SoapRecipe> {

	private static final long serialVersionUID = 1L;
	private RecipeEntryList<Fat> fats;
	private RecipeEntryList<Acid> acids;
	private RecipeEntryList<Liquid> liquids;
	private RecipeEntryList<Fragrance> fragrances;
	private SoapRecipe soapRecipe;

	public RecipeDetailsPanel(EntityViewDetailsControllerCallback<SoapRecipe> callback, FatRepository fatRepository,
			AcidRepository acidRepository, LiquidRepository liquidRepository, FragranceRepository fragranceRepository) {
		super(callback);

		addPropertyPercentageField("domain.recipe.superfat", SoapRecipe::getSuperFat, SoapRecipe::setSuperFat);
		addPropertyPercentageField("domain.recipe.liquidtofatratio", SoapRecipe::getLiquidToFatRatio,
				SoapRecipe::setLiquidToFatRatio);
		addPropertyPercentageField("domain.recipe.fragrancetotal", SoapRecipe::getFragranceTotal,
				SoapRecipe::setFragranceTotal);
		addPropertyTextArea("domain.recipe.notes", SoapRecipe::getNotes, SoapRecipe::setNotes);
		addPropertyPercentageField("domain.recipe.naohtokohratio", SoapRecipe::getNaOHToKOHRatio,
				SoapRecipe::setNaOHToKOHRatio);
		addPropertyPercentageField("domain.recipe.kohpurity", SoapRecipe::getKOHPurity, SoapRecipe::setKOHPurity);

		fats = new RecipeEntryList<Fat>(fatRepository, "domain.fats");
		// TODO add panel the same way as the propertypanels
		fats.setSizeFull();
		getContent().add(fats);

		acids = new RecipeEntryList<>(acidRepository, "domain.acids");
		acids.setSizeFull();
		getContent().add(acids);

		liquids = new RecipeEntryList<>(liquidRepository, "domain.liquids");
		liquids.setSizeFull();
		getContent().add(liquids);

		fragrances = new RecipeEntryList<>(fragranceRepository, "domain.fragrances");
		fragrances.setSizeFull();
		getContent().add(fragrances);

	}

	private Component wrapInCollapsableLayout(Component component) {
		CollapsableLayout collapsableLayout = new CollapsableLayout();
		getContent().add(collapsableLayout);
		// A border to show the outline of the layout itself
		collapsableLayout.getElement().getStyle().set("border", "1px solid #aaa");
		collapsableLayout.addContentComponent(component);
		// Add a header caption
		collapsableLayout.addHeaderComponent(new Span("Collapsable layout with custom header"));
		// Add a header button that toggles the visibility on click
		Button collapseButton = new Button("Show", e -> collapsableLayout.toggleContentVisibility());
		collapsableLayout.addHeaderComponentAsLastAndAlignToRight(collapseButton);

		// Change the button caption based on the collapse state change
		collapsableLayout
				.addCollapseChangeListener(e -> collapseButton.setText(e.isCurrentlyVisible() ? "Hide" : "Show"));
		return collapsableLayout;
	}

	@Override
	protected void setEntity(SoapRecipe soapRecipe) {
		this.soapRecipe = soapRecipe;
		if (soapRecipe == null) {
			fats.setData();
			acids.setData();
			liquids.setData();
			fragrances.setData();
		} else {
			fats.setData(soapRecipe.getFats());
			acids.setData(soapRecipe.getAcids());
			liquids.setData(soapRecipe.getLiquids());
			fragrances.setData(soapRecipe.getFragrances());
		}
	}

	@Override
	protected void preSave() {
		super.preSave();

		soapRecipe.setFats(List.copyOf(fats.getData()));
		soapRecipe.setAcids(List.copyOf(acids.getData()));
		soapRecipe.setLiquids(List.copyOf(liquids.getData()));
		soapRecipe.setFragrances(List.copyOf(fragrances.getData()));
	}

	@Override
	protected void enterEditMode() {
		super.enterEditMode();
		fats.enterEditMode();
		acids.enterEditMode();
		liquids.enterEditMode();
		fragrances.enterEditMode();
	}

	@Override
	protected void leaveEditMode() {
		super.leaveEditMode();
		fats.leaveEditMode();
		acids.leaveEditMode();
		liquids.leaveEditMode();
		fragrances.leaveEditMode();
	}

}
