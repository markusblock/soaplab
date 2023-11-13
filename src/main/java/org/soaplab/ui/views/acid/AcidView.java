package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.domain.Fat;
import org.soaplab.repository.AcidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "acid", layout = MainAppLayout.class)
public class AcidView extends IngredientTableViewParent<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidView(AcidRepository repository) {
		super(Acid.class, repository);

		addBigDecimalColumn(Fat.Fields.sapNaoh, "domain.ingredient.sapnaoh");
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.acids");
	}

	@Override
	protected Acid createNewEmptyEntity() {
		return Acid.builder().build();
	}
}
