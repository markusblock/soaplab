package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "fragrance", layout = MainAppLayout.class)
public class FragranceTablePanel extends IngredientTablePanel<Fragrance> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FragranceTablePanel(EntityTableListener<Fragrance> listener) {
		super(Fragrance.class, listener);
	}
}
