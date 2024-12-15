package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "liquids", layout = MainAppLayout.class)
public class LiquidsView extends EntityView<Liquid> {

	private static final long serialVersionUID = 1L;
	private LiquidRepository repository;

	@Autowired
	public LiquidsView(LiquidRepository repository) {
		super(repository, "domain.liquids");
		this.repository = repository;
	}

	@Override
	protected IngredientTablePanel<Liquid> createEntityTable(EntityTableListener<Liquid> listener) {
		return new LiquidTablePanel(repository, listener);
	}

	@Override
	protected LiquidDetailsPanel createEntityDetails(EntityDetailsListener<Liquid> listener) {
		return new LiquidDetailsPanel(listener);
	}

	@Override
	protected Liquid createNewEmptyEntity() {
		return Liquid.builder().build();
	}
}
