package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "acids2", layout = MainAppLayout.class)
public class AcidsViewOld extends EntityView<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidsViewOld(AcidRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.acids");
	}

	@Override
	protected IngredientList<Acid> createEntityList(EntityViewListControllerCallback<Acid> callback) {
		return new IngredientList<Acid>(callback);
	}

	@Override
	protected AcidDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<Acid> callback) {
		return new AcidDetailsPanel(callback);
	}

	@Override
	protected Acid createNewEmptyEntity() {
		return Acid.builder().build();
	}
}
