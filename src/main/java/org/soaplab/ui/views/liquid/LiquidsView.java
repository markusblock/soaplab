package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientList;
import org.soaplab.ui.views.IngredientsView;
import org.soaplab.ui.views.IngredientsViewControllerCallback;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "liquids", layout = MainAppLayout.class)
public class LiquidsView extends IngredientsView<Liquid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public LiquidsView(LiquidRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.liquids");
	}

	@Override
	protected IngredientList<Liquid> createIngredientList(IngredientsViewControllerCallback<Liquid> callback) {
		return new IngredientList<Liquid>(callback);
	}

	@Override
	protected LiquidDetailsPanel createIngredientDetails(IngredientsViewControllerCallback<Liquid> callback) {
		return new LiquidDetailsPanel(callback);
	}

	@Override
	protected Liquid createNewEntity() {
		return Liquid.builder().build();
	}
}
