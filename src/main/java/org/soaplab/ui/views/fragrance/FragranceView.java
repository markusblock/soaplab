package org.soaplab.ui.views.fragrance;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.IngredientTableViewParent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fragrance", layout = MainAppLayout.class)
public class FragranceView extends IngredientTableViewParent<Fragrance> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FragranceView(FragranceRepository repository) {
		super(Fragrance.class, repository);
	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fragrances");
	}

	@Override
	protected Fragrance createNewEmptyEntity() {
		return Fragrance.builder().build();
	}
}
