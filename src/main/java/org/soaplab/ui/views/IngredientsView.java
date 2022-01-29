package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

public abstract class IngredientsView<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;
	private H1 title;
	private IngredientGrid<T> grid;

	public IngredientsView(IngredientRepository<T> repository) {
		content = new VerticalLayout();

		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		content.add(title);

		grid = createIngredientGrid();
		content.add(grid);

		add(content);

		grid.setItems(new ListDataProvider<T>(repository.findAll()));
	}

	protected abstract String getHeader();

	protected abstract IngredientGrid<T> createIngredientGrid();

}
