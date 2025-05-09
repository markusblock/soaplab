package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "acid", layout = MainAppLayout.class)
public class AcidTablePanel extends IngredientTablePanel<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidTablePanel(EntityTableListener<Acid> listener) {
		super(Acid.class, listener);

		addBigDecimalColumn(Fat.Fields.sapNaoh, "domain.ingredient.sapnaoh");
	}

}
