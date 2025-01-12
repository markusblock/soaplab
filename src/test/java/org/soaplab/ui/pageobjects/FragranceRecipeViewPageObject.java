package org.soaplab.ui.pageobjects;

import org.soaplab.domain.FragranceRecipe;

public class FragranceRecipeViewPageObject
		extends EntityViewPageObject<EntityTablePanelPageObject, FragranceRecipeDetailsPageObject> {

	private final EntityTablePanelPageObject entityTable;
	private final FragranceRecipeDetailsPageObject entityDetails;

	public FragranceRecipeViewPageObject() {
		entityTable = new EntityTablePanelPageObject("entitygrid.%s".formatted(FragranceRecipe.class.getSimpleName()));
		entityDetails = new FragranceRecipeDetailsPageObject();
	}

	@Override
	public FragranceRecipeDetailsPageObject getEntityDetails() {
		return entityDetails;
	}

	@Override
	public EntityTablePanelPageObject getEntityTable() {
		return entityTable;
	}
}
