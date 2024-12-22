package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "additive", layout = MainAppLayout.class)
public class AdditiveTablePanel extends IngredientTablePanel<Additive> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AdditiveTablePanel(EntityTableListener<Additive> listener) {
		super(Additive.class, listener);

		addBigDecimalColumn(Additive.Fields.sapNaoh, "domain.ingredient.sapnaoh");
	}
}
