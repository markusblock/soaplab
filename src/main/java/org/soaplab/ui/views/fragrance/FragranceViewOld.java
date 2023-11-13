package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fragrances2", layout = MainAppLayout.class)
public class FragranceViewOld extends EntityView<Fragrance> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FragranceViewOld(FragranceRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fragrances");
	}

	@Override
	protected IngredientList<Fragrance> createEntityList(EntityViewListControllerCallback<Fragrance> callback) {
		return new IngredientList<Fragrance>(callback);
	}

	@Override
	protected FragranceDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<Fragrance> callback) {
		return new FragranceDetailsPanel(callback);
	}

	@Override
	protected Fragrance createNewEmptyEntity() {
		return Fragrance.builder().build();
	}
}
