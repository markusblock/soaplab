package org.soaplab.ui.views;

import java.util.List;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class IngredientsView<T extends Ingredient> extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private H1 title;

	private IngredientDetailsPanel<T> detailsPanel;
	private IngredientGrid<T> ingredientGrid;
	private IngredientRepository<T> repository;

	public IngredientsView(IngredientRepository<T> repository) {
		super();
		this.repository = repository;

		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		add(title);

		HorizontalLayout masterDetail = new HorizontalLayout();
		masterDetail.setSizeFull();

		ingredientGrid = createIngredientGrid();
		ingredientGrid.setMinWidth(50, Unit.PERCENTAGE);
		addSelectionListener();
		masterDetail.add(ingredientGrid);
		detailsPanel = createIngredientDetailsPanel();
		masterDetail.add(detailsPanel);

		masterDetail.setFlexGrow(0.8, ingredientGrid);
		add(masterDetail);
	}

	private void addSelectionListener() {
		ingredientGrid.setSelectionMode(SelectionMode.SINGLE);
		SingleSelect<Grid<T>, T> ingredientSelect = ingredientGrid.asSingleSelect();
		ingredientSelect.addValueChangeListener(e -> {
			T selectedIngredient = e.getValue();
			detailsPanel.setData(selectedIngredient);
		});

	}

	protected abstract String getHeader();

	protected abstract IngredientGrid<T> createIngredientGrid();

	protected abstract IngredientDetailsPanel<T> createIngredientDetailsPanel();

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		ingredientGrid.setItems(getDataProvider());
		refreshGrid();
	}

	private void refreshGrid() {
		ingredientGrid.select(null);
		ingredientGrid.getLazyDataView().refreshAll();
	}

	CallbackDataProvider<T, Void> getDataProvider() {
		return DataProvider.fromCallbacks(
				// First callback fetches items based on a query
				query -> {
					// The index of the first item to load
					int offset = query.getOffset();

					// The number of items to load
					int limit = query.getLimit();
					List<T> persons = repository.findAll();
					return persons.stream();
				},
				// Second callback fetches the total number of items currently in the Grid.
				// The grid can then use it to properly adjust the scrollbars.
				query -> repository.findAll().size());
	}
}
