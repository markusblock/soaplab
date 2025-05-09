package org.soaplab.ui.views.fat;

import org.soaplab.domain.Fat;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.IngredientTablePanel;
import org.springframework.beans.factory.annotation.Autowired;

//@Route(value = "fat", layout = MainAppLayout.class)
//@RouteAlias(value = "", layout = MainAppLayout.class) // registers on the root path of the server, but doesn'T work together with Swagger
public class FatTablePanel extends IngredientTablePanel<Fat> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public FatTablePanel(EntityTableListener<Fat> listener) {
		super(Fat.class, listener);

		addIntegerColumn(Fat.Fields.ins, "domain.fat.ins");
		addBigDecimalColumn(Fat.Fields.sapNaoh, "domain.ingredient.sapnaoh");
		addIntegerColumn(Fat.Fields.iodine, "domain.fat.iodine");
		addIntegerColumn(Fat.Fields.lauric, "domain.fat.lauric");
		addIntegerColumn(Fat.Fields.myristic, "domain.fat.myristic");
		addIntegerColumn(Fat.Fields.palmitic, "domain.fat.palmitic");
		addIntegerColumn(Fat.Fields.stearic, "domain.fat.stearic");
		addIntegerColumn(Fat.Fields.ricinoleic, "domain.fat.ricinoleic");
		addIntegerColumn(Fat.Fields.oleic, "domain.fat.oleic");
		addIntegerColumn(Fat.Fields.linoleic, "domain.fat.linoleic");
		addIntegerColumn(Fat.Fields.linolenic, "domain.fat.linolenic");
	}

}
