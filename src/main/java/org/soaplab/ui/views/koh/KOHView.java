package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "koh", layout = MainAppLayout.class)
public class KOHView extends IngredientTableViewParent<KOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public KOHView(KOHRepository repository) {
		super(KOH.class, repository);

		// addPercentageColumn(KOH.Fields.kOHPurity, "domain.koh.kohpurity");
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.koh");
	}

	@Override
	protected KOH createNewEmptyEntity() {
		return KOH.builder().build();
	}
}
