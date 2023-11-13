package org.soaplab.ui.views.naoh;

import org.soaplab.domain.NaOH;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "naoh", layout = MainAppLayout.class)
public class NaOHView extends IngredientTableViewParent<NaOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public NaOHView(NaOHRepository repository) {
		super(NaOH.class, repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.naoh");
	}

	@Override
	protected NaOH createNewEmptyEntity() {
		return NaOH.builder().build();
	}
}
