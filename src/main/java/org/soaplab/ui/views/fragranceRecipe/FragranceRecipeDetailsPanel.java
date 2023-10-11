package org.soaplab.ui.views.fragranceRecipe;

import java.util.List;

import org.soaplab.domain.*;
import org.soaplab.repository.*;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.RecipeEntryList;
import org.springframework.util.CollectionUtils;

public class FragranceRecipeDetailsPanel extends EntityDetails<FragranceRecipe> {

	private static final long serialVersionUID = 1L;
	private final RecipeEntryList<Fragrance> fragrances;

	private FragranceRecipe recipe;

	public FragranceRecipeDetailsPanel(EntityViewDetailsControllerCallback<FragranceRecipe> callback, FragranceRepository fragranceRepository) {
		super(callback);

		addPropertyTextArea("domain.recipe.notes", FragranceRecipe::getNotes, FragranceRecipe::setNotes);

		fragrances = new RecipeEntryList<>(fragranceRepository, "domain.fragrances");
		fragrances.setWidthFull();
		addContent(fragrances);
	}

	@Override
	protected void enterEditMode() {
		super.enterEditMode();
		fragrances.enterEditMode();
	}

	@Override
	protected void leaveEditMode() {
		super.leaveEditMode();
		fragrances.leaveEditMode();
	}

	@Override
	protected void preSave() {
		super.preSave();
		recipe.setFragrances(List.copyOf(fragrances.getData()));
	}

	@Override
	protected void setEntity(FragranceRecipe entity) {
		this.recipe = entity;

		if (recipe == null) {
			fragrances.setData();
		} else {
			fragrances.setData(recipe.getFragrances());
		}
	}
}
