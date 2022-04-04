package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientList;
import org.soaplab.ui.views.IngredientsView;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;
import org.soaplab.ui.views.IngredientsViewListControllerCallback;
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
	protected IngredientList<Fragrance> createIngredientList(
			IngredientsViewListControllerCallback<Fragrance> callback) {
		return new IngredientList<Fragrance>(callback);
	}

	@Override
	protected FragranceDetailsPanel createIngredientDetails(
			IngredientsViewDetailsControllerCallback<Fragrance> callback) {
		return new FragranceDetailsPanel(callback);
	}

	@Override
	protected Fragrance createNewEmptyIngredient() {
		return Fragrance.builder().build();
	}
}
