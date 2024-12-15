package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fragrances", layout = MainAppLayout.class)
public class FragrancesView extends EntityView<Fragrance> {

	private static final long serialVersionUID = 1L;
	private FragranceRepository repository;

	@Autowired
	public FragrancesView(FragranceRepository repository) {
		super(repository, "domain.fragrances");
		this.repository = repository;
	}

	@Override
	protected IngredientTablePanel<Fragrance> createEntityTable(EntityTableListener<Fragrance> listener) {
		return new FragranceTablePanel(repository, listener);
	}

	@Override
	protected FragranceDetailsPanel createEntityDetails(EntityDetailsListener<Fragrance> listener) {
		return new FragranceDetailsPanel(listener);
	}

	@Override
	protected Fragrance createNewEmptyEntity() {
		return Fragrance.builder().build();
	}
}
