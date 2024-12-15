package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "additives", layout = MainAppLayout.class)
public class AdditivesView extends EntityView<Additive> {

	private static final long serialVersionUID = 1L;
	private AdditiveRepository repository;

	@Autowired
	public AdditivesView(AdditiveRepository repository) {
		super(repository, "domain.additives");
		this.repository = repository;
	}

	@Override
	protected IngredientTablePanel<Additive> createEntityTable(EntityTableListener<Additive> listener) {
		return new AdditiveTablePanel(repository, listener);
	}

	@Override
	protected AdditiveDetailsPanel createEntityDetails(EntityDetailsListener<Additive> listener) {
		return new AdditiveDetailsPanel(listener);
	}

	@Override
	protected Additive createNewEmptyEntity() {
		return Additive.builder().build();
	}
}
