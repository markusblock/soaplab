package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityDetailsListener;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fats", layout = MainAppLayout.class)
//@RouteAlias(value = "", layout = MainAppLayout.class) // registers on the root path of the server, but doesn'T work
// together with Swagger
public class FatsView extends EntityView<Fat> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FatsView(FatRepository repository) {
		super(repository, "domain.fats");
	}

	@Override
	protected IngredientTablePanel<Fat> createEntityTable(EntityTableListener<Fat> listener) {
		return new FatTablePanel(listener);
	}

	@Override
	protected FatDetailsPanel createEntityDetails(EntityDetailsListener<Fat> callback) {
		return new FatDetailsPanel(callback);
	}

	@Override
	protected Fat createNewEmptyEntity() {
		return Fat.builder().build();
	}

}
