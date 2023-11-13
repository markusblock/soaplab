package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "koh2", layout = MainAppLayout.class)
public class KOHViewOld extends EntityView<KOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public KOHViewOld(KOHRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.koh");
	}

	@Override
	protected IngredientList<KOH> createEntityList(EntityViewListControllerCallback<KOH> callback) {
		return new IngredientList<KOH>(callback);
	}

	@Override
	protected KOHDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<KOH> callback) {
		return new KOHDetailsPanel(callback);
	}

	@Override
	protected KOH createNewEmptyEntity() {
		return KOH.builder().build();
	}
}
