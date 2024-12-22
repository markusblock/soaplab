package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "koh", layout = MainAppLayout.class)
public class KOHView extends EntityView<KOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public KOHView(KOHRepository repository) {
		super(repository, "domain.koh");
	}

	@Override
	protected IngredientTablePanel<KOH> createEntityTable(EntityTableListener<KOH> listener) {
		return new KOHTablePanel(listener);
	}

	@Override
	protected KOHDetailsPanel createEntityDetails(EntityDetailsListener<KOH> callback) {
		return new KOHDetailsPanel(callback);
	}

	@Override
	protected KOH createNewEmptyEntity() {
		return KOH.builder().build();
	}
}
