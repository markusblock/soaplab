package org.soaplab.ui.views.naoh;

import org.soaplab.domain.NaOH;
import org.soaplab.repository.NaOHRepository;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "naoh", layout = MainAppLayout.class)
public class NaOHTablePanel extends IngredientTablePanel<NaOH> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public NaOHTablePanel(NaOHRepository repository, EntityTableListener<NaOH> listener) {
		super(NaOH.class, repository, listener);
	}
}
