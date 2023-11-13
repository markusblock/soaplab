package org.soaplab.ui.views.naoh;

import org.soaplab.domain.NaOH;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "naoh2", layout = MainAppLayout.class)
public class NaOHViewOld extends EntityView<NaOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public NaOHViewOld(NaOHRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.naoh");
	}

	@Override
	protected IngredientList<NaOH> createEntityList(EntityViewListControllerCallback<NaOH> callback) {
		return new IngredientList<NaOH>(callback);
	}

	@Override
	protected NaOHDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<NaOH> callback) {
		return new NaOHDetailsPanel(callback);
	}

	@Override
	protected NaOH createNewEmptyEntity() {
		return NaOH.builder().build();
	}
}
