package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "liquid", layout = MainAppLayout.class)
public class LiquidTablePanel extends IngredientTablePanel<Liquid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public LiquidTablePanel(LiquidRepository repository, EntityTableListener<Liquid> listener) {
		super(Liquid.class, repository, listener);
	}
}
