package org.soaplab.ui.views.fragranceRecipe;

import java.util.List;
import java.util.Optional;

import org.soaplab.domain.Fragrance;
import org.soaplab.domain.FragranceRecipe;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityDetailsPanel;
import org.soaplab.ui.views.RecipeEntryList;

public class FragranceRecipeDetailsPanel extends EntityDetailsPanel<FragranceRecipe> {

	private static final long serialVersionUID = 1L;
	private final RecipeEntryList<Fragrance> fragrances;

	private Optional<FragranceRecipe> recipe;

	public FragranceRecipeDetailsPanel(EntityDetailsListener<FragranceRecipe> listener,
			FragranceRepository fragranceRepository) {
		super(listener);

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
	protected void updateEntityWithChangesFromUI() {
		super.updateEntityWithChangesFromUI();
		recipe.orElseThrow().setFragrances(List.copyOf(fragrances.getData()));
	}

	@Override
	protected void setEntity(Optional<FragranceRecipe> entity) {
		this.recipe = entity;

		entity.ifPresentOrElse(t -> fragrances.setData(t.getFragrances()), () -> fragrances.setData());
	}
}
