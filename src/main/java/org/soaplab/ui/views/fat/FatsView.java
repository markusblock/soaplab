package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientGrid;
import org.soaplab.ui.views.IngredientsView;
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
	protected IngredientGrid<Fat> createIngredientGrid() {
		return new FatGrid();
	}
}
