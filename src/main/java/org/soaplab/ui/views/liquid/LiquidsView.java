package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientGrid;
import org.soaplab.ui.views.IngredientsView;
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
	protected IngredientGrid<Liquid> createIngredientGrid() {
		return new LiquidGrid();
	}
}
