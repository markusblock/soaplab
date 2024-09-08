package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.soaplab.ui.views.PercentageRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.router.Route;

@Route(value = "koh", layout = MainAppLayout.class)
public class KOHView extends IngredientTableViewParent<KOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public KOHView(KOHRepository repository) {
		super(KOH.class, repository);
		
		Column<KOH> kohPurityColumn = addPercentageColumn(KOH.Fields.kohPurity, "domain.koh.kohpurity");
		kohPurityColumn.setRenderer(new PercentageRenderer<KOH>(KOH::getKohPurity));
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
