package org.soaplab.ui.views.ingredient;

import org.soaplab.domain.Ingredient;
import org.soaplab.ui.views.EntityTableListener;
import org.soaplab.ui.views.EntityTablePanel;
import org.soaplab.ui.views.PriceRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid.Column;

//@Route(value = "ingredient", layout = MainAppLayout.class)
public class AllIngredientsTablePanel extends EntityTablePanel<Ingredient> {

	private static final long serialVersionUID = 1L;

	@Autowired
	public AllIngredientsTablePanel(EntityTableListener<Ingredient> listener) {
		super(Ingredient.class, listener);

		addEntityColumn(Ingredient.Fields.inci, "domain.ingredient.inci");

		final Column<Ingredient> priceColumn = addPriceColumn(Ingredient.Fields.cost, "domain.ingredient.price");
		priceColumn.setRenderer(new PriceRenderer<Ingredient>(Ingredient::getCost));
	}
}
