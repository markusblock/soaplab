package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientList;
import org.soaplab.ui.views.IngredientsView;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;
import org.soaplab.ui.views.IngredientsViewListControllerCallback;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fats", layout = MainAppLayout.class)
public class FatsView extends IngredientsView<Fat> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FatsView(FatRepository repository) {
		super(repository);

	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fats");
	}

	@Override
	protected IngredientList<Fat> createIngredientList(IngredientsViewListControllerCallback<Fat> callback) {
		return new IngredientList<Fat>(callback);
	}

	@Override
	protected FatDetailsPanel createIngredientDetails(IngredientsViewDetailsControllerCallback<Fat> callback) {
		return new FatDetailsPanel(callback);
	}

	@Override
	protected Fat createNewEmptyIngredient() {
		return Fat.builder().build();
	}

}
