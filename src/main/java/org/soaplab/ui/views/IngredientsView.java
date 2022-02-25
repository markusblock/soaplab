package org.soaplab.ui.views;

import java.util.List;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class IngredientsView<T extends Ingredient> extends VerticalLayout
		implements BeforeEnterObserver, IngredientsViewControllerCallback<T> {

	private static final long serialVersionUID = 1L;

	private H1 title;

	private IngredientDetails<T> ingredientDetails;
	private IngredientList<T> ingredientList;
	private IngredientRepository<T> repository;

	public IngredientsView(IngredientRepository<T> repository) {
		super();
		this.repository = repository;

		title = new H1(getHeader());
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		add(title);

		HorizontalLayout masterDetail = new HorizontalLayout();
		masterDetail.setSizeFull();

		ingredientList = createIngredientList(this);
		ingredientList.setMinWidth(50, Unit.PERCENTAGE);
		addSelectionListener();
		masterDetail.add(ingredientList);
		ingredientDetails = createIngredientDetails(this);
		masterDetail.add(ingredientDetails);

		masterDetail.setFlexGrow(0.8, ingredientList);
		add(masterDetail);
	}

	private void addSelectionListener() {
		ingredientList.addSelectionListener(e -> {
			T selectedIngredient = e.getValue();
			ingredientDetails.setIngredient(selectedIngredient);
		});
	}

	protected abstract String getHeader();

	protected abstract IngredientList<T> createIngredientList(IngredientsViewControllerCallback<T> callback);

	protected abstract IngredientDetails<T> createIngredientDetails(IngredientsViewControllerCallback<T> callback);

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		ingredientList.setItems(getDataProvider());
		ingredientList.selectFirstIngredient();
	}

	@Override
	public void onSaveIngredient(T ingredient) {
		if (ingredient.getId() == null) {
			repository.create(ingredient);
			ingredientList.refreshAll();
			ingredientList.select(ingredient);
		} else {
			repository.update(ingredient);
		}
	}

	@Override
	public void onCreateNewIngredient() {
		T newEntity = createNewEntity();
		ingredientList.select(null);
		ingredientDetails.createIngredient(newEntity);
	}

	protected abstract T createNewEntity();

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
