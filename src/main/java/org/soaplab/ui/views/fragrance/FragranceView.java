package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientsView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fragrances", layout = MainAppLayout.class)
public class FragranceView extends IngredientsView<Fragrance> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FragranceView(FragranceRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fragrances");
	}

	@Override
	protected FragranceGrid createIngredientGrid() {
		return new FragranceGrid();
	}

	@Override
	protected FragranceDetailsPanel createIngredientDetailsPanel() {
		return new FragranceDetailsPanel();
	}
}
