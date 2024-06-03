package org.soaplab.ui.views.additives;

import org.soaplab.domain.Additive;
import org.soaplab.repository.AdditiveRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "additive", layout = MainAppLayout.class)
public class AdditiveView extends IngredientTableViewParent<Additive> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AdditiveView(AdditiveRepository repository) {
		super(Additive.class, repository);

		addBigDecimalColumn(Additive.Fields.sapNaoh, "domain.ingredient.sapnaoh");
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.additives");
	}

	@Override
	protected Additive createNewEmptyEntity() {
		return Additive.builder().build();
	}
}
