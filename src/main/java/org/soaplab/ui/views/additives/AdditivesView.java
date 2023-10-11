package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.domain.Liquid;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "additives", layout = MainAppLayout.class)
public class AdditivesView extends EntityView<Additive> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AdditivesView(AdditiveRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.additives");
	}

	@Override
	protected IngredientList<Additive> createEntityList(EntityViewListControllerCallback<Additive> callback) {
		return new IngredientList<Additive>(callback);
	}

	@Override
	protected AdditiveDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<Additive> callback) {
		return new AdditiveDetailsPanel(callback);
	}

	@Override
	protected Additive createNewEmptyEntity() {
		return Additive.builder().build();
	}
}
