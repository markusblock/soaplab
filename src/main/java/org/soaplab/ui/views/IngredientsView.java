package org.soaplab.ui.views;

import java.util.Optional;
import java.util.UUID;

import org.soaplab.domain.Ingredient;
import org.soaplab.repository.IngredientRepository;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class IngredientsView<T extends Ingredient> extends VerticalLayout implements BeforeEnterObserver,
		IngredientsViewListControllerCallback<T>, IngredientsViewDetailsControllerCallback<T> {

	private static final long serialVersionUID = 1L;

	private H1 title;

	private IngredientDetails<T> ingredientDetails;
	private IngredientList<T> ingredientList;
	private IngredientRepository<T> repository;

	private Optional<T> selectedEntity = Optional.empty();

	private boolean editNewIngredientMode;

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
		masterDetail.add(ingredientList);
		ingredientDetails = createIngredientDetails(this);
		masterDetail.add(ingredientDetails);

		masterDetail.setFlexGrow(0.8, ingredientList);
		add(masterDetail);
	}

	protected abstract String getHeader();

	protected abstract IngredientList<T> createIngredientList(IngredientsViewListControllerCallback<T> callback);

	protected abstract IngredientDetails<T> createIngredientDetails(
			IngredientsViewDetailsControllerCallback<T> callback);

	@Override
	public IngredientRepository<T> getRepository() {
		return repository;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		ingredientList.selectFirstIngredient();
	}

	@Override
	public void saveIngredient(T ingredient) {
		if (editNewIngredientMode) {
			UUID uuid = repository.create(ingredient);
			ingredientList.refreshAll();
			ingredient = repository.get(uuid);
		} else {
			repository.update(ingredient);
			ingredientList.refresh(ingredient);
		}
		editNewIngredientMode = false;
		ingredientList.listenToSelectionChanges();
		ingredientList.select(ingredient);
		ingredientDetails.showIngredient(ingredient);
	}

	@Override
	public void deleteIngredient(T ingredient) {
		repository.delete(ingredient.getId());
		ingredientList.refreshAll();
		ingredientList.selectFirstIngredient();
	}

	@Override
	public void editIngredient(T ingredient) {
		ingredientList.ignoreSelectionChanges();
		ingredientDetails.editIngredient(ingredient);
	}

	@Override
	public void cancelEditMode() {
		editNewIngredientMode = false;
		ingredientList.listenToSelectionChanges();
		if (selectedEntity.isPresent()) {
			ingredientList.select(selectedEntity.get());
			ingredientDetails.showIngredient(selectedEntity.get());
		} else {
			ingredientList.selectFirstIngredient();
		}
	}

	@Override
	public void createNewIngredient() {
		editNewIngredientMode = true;
		ingredientList.ignoreSelectionChanges();
		Optional<T> selectedEntity = ingredientList.getSelectedEntity();
		T newEntity = createNewEmptyIngredient();
		ingredientList.deselectAll();
		ingredientDetails.editIngredient(newEntity);
		this.selectedEntity = selectedEntity;
	}

	@Override
	public void ingredientSelected(T ingredient) {
		selectedEntity = Optional.ofNullable(ingredient);
		ingredientDetails.showIngredient(ingredient);
	}

	protected abstract T createNewEmptyIngredient();
}
