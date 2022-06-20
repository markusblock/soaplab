package org.soaplab.ui.views.recipe;

import org.soaplab.domain.Fat;
import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.views.EntityDetails;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;

public class RecipeDetailsPanel extends EntityDetails<SoapRecipe> {

	private static final long serialVersionUID = 1L;
	private RecipeEntryList<Fat> fats;

	public RecipeDetailsPanel(EntityViewDetailsControllerCallback<SoapRecipe> callback, FatRepository fatRepository) {
		super(callback);

		fats = new RecipeEntryList<Fat>(fatRepository);
		fats.setSizeFull();
		// TODO add panel the same way as the propertypanels
		getContent().add(fats);
	}

	@Override
	protected void setEntity(SoapRecipe soapRecipe) {
		if (soapRecipe == null) {
			fats.setData();
		} else {
			fats.setData(soapRecipe.getFats().values());
		}
	}

	@Override
	protected void enterEditMode() {
		// TODO Auto-generated method stub
		super.enterEditMode();
		fats.enterEditMode();
	}

	@Override
	protected void leaveEditMode() {
		// TODO Auto-generated method stub
		super.leaveEditMode();
		fats.leaveEditMode();
	}

}
