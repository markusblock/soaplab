package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "acid", layout = MainAppLayout.class)
public class AcidsView extends EntityView<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidsView(AcidRepository repository) {
		super(repository, "domain.acids");
	}

	@Override
	protected IngredientTablePanel<Acid> createEntityTable(EntityTableListener<Acid> listener) {
		return new AcidTablePanel(listener);
	}

	@Override
	protected AcidDetailsPanel createEntityDetails(EntityDetailsListener<Acid> listener) {
		return new AcidDetailsPanel(listener);
	}

	@Override
	protected Acid createNewEmptyEntity() {
		return Acid.builder().build();
	}
}
