package org.soaplab.ui.views.koh;

import org.soaplab.domain.KOH;
import org.soaplab.repository.KOHRepository;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.soaplab.ui.views.PercentageRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid.Column;

//@Route(value = "koh", layout = MainAppLayout.class)
public class KOHTablePanel extends IngredientTablePanel<KOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public KOHTablePanel(KOHRepository repository, EntityTableListener<KOH> listener) {
		super(KOH.class, repository, listener);

		Column<KOH> kohPurityColumn = addPercentageColumn(KOH.Fields.kohPurity, "domain.koh.kohpurity");
		kohPurityColumn.setRenderer(new PercentageRenderer<KOH>(KOH::getKohPurity));
	}
}
