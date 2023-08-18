package org.soaplab.ui.views.ingredient;

import org.soaplab.service.ingredients.IngredientsService;
import org.soaplab.ui.MainAppLayout;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "ingredients", layout = MainAppLayout.class)
public class IngredientView extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private final IngredientsService ingredientService;

	@Autowired
	public IngredientView(IngredientsService ingredientsService) {
		ingredientService = ingredientsService;
	}

	private H1 title;
	private IngredientList ingredientList;

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		removeAll();

		title = new H1("Ingredients");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		add(title);

		ingredientList = new IngredientList(ingredientService);
		ingredientList.setWidthFull();
		add(ingredientList);
	}

}
