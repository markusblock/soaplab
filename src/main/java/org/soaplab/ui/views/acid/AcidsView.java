package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientList;
import org.soaplab.ui.views.IngredientsView;
import org.soaplab.ui.views.IngredientsViewDetailsControllerCallback;
import org.soaplab.ui.views.IngredientsViewListControllerCallback;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "acids", layout = MainAppLayout.class)
public class AcidsView extends IngredientsView<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidsView(AcidRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.acids");
	}

	@Override
	protected AcidDetailsPanel createIngredientDetails(IngredientsViewDetailsControllerCallback<Acid> callback) {
		return new AcidDetailsPanel(callback);
	}

	@Override
	protected IngredientList<Acid> createIngredientList(IngredientsViewListControllerCallback<Acid> callback) {
		return new IngredientList<Acid>(callback);
	}

	@Override
	protected Acid createNewEmptyIngredient() {
		return Acid.builder().build();
	}
}
