package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.soaplab.ui.MainAppLayout;
import org.soaplab.ui.views.EntityView;
import org.soaplab.ui.views.EntityViewDetailsControllerCallback;
import org.soaplab.ui.views.EntityViewListControllerCallback;
import org.soaplab.ui.views.IngredientList;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

@Route(value = "fats2", layout = MainAppLayout.class)
//@RouteAlias(value = "", layout = MainAppLayout.class) // registers on the root path of the server, but doesn'T work together with Swagger
public class FatsViewOld extends EntityView<Fat> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FatsViewOld(FatRepository repository) {
		super(repository);

	}

	@Override
	protected String getHeader() {
		return getTranslation("domain.fats");
	}

	@Override
	protected IngredientList<Fat> createEntityList(EntityViewListControllerCallback<Fat> callback) {
		return new IngredientList<Fat>(callback);
	}

	@Override
	protected FatDetailsPanel createEntityDetails(EntityViewDetailsControllerCallback<Fat> callback) {
		return new FatDetailsPanel(callback);
	}

	@Override
	protected Fat createNewEmptyEntity() {
		return Fat.builder().build();
	}

}
