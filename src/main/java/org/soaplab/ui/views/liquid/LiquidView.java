package org.soaplab.ui.views.liquid;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "liquid", layout = MainAppLayout.class)
public class LiquidView extends IngredientTableViewParent<Liquid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public LiquidView(LiquidRepository repository) {
		super(Liquid.class, repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.liquids");
	}

	@Override
	protected Liquid createNewEmptyEntity() {
		return Liquid.builder().build();
	}
}
