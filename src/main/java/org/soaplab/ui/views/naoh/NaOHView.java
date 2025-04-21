package org.soaplab.ui.views.naoh;

import org.soaplab.domain.NaOH;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "naoh", layout = MainAppLayout.class)
public class NaOHView extends EntityView<NaOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public NaOHView(NaOHRepository repository) {
		super(repository, "domain.naoh");
	}

	@Override
	protected IngredientTablePanel<NaOH> createEntityTable(EntityTableListener<NaOH> listener) {
		return new NaOHTablePanel(listener);
	}

	@Override
	protected NaOHDetailsPanel createEntityDetails(EntityDetailsListener<NaOH> listener) {
		return new NaOHDetailsPanel(listener);
	}

	@Override
	protected NaOH createNewEmptyEntity() {
		return NaOH.builder().build();
	}
}
