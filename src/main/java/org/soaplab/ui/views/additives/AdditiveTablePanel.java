package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "additive", layout = MainAppLayout.class)
public class AdditiveTablePanel extends IngredientTablePanel<Additive> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AdditiveTablePanel(AdditiveRepository repository, EntityTableListener<Additive> listener) {
		super(Additive.class, repository, listener);

		addBigDecimalColumn(Additive.Fields.sapNaoh, "domain.ingredient.sapnaoh");
	}
}
