package org.soaplab.ui.views.acid;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientsView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "acids", layout = MainAppLayout.class)
public class AcidsView extends IngredientsView<Acid> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AcidsView(AcidRepository repository) {
		super(repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.acids");
	}

	@Override
	protected AcidDetailsPanel createIngredientDetailsPanel() {
		return new AcidDetailsPanel();
	}

	@Override
	protected AcidGrid createIngredientGrid() {
		return new AcidGrid();
	}
}
