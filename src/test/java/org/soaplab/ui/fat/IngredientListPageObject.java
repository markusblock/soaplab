package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static org.soaplab.ui.fat.VaadinUtils.selected;

import java.util.Optional;

import org.soaplab.domain.Ingredient;

import com.codeborne.selenide.SelenideElement;

public class IngredientListPageObject {

	private VaadinGrid grid;

	public IngredientListPageObject() {
		grid = new VaadinGrid(byId("ingredientlist.grid"));
	}

	public IngredientListPageObject ingredientShouldAppear(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		return this;
	}

	public IngredientListPageObject ingredientShouldNotAppear(String name) {
		grid.firstColumnShouldNotContain(name);
		return this;
	}

	public IngredientListPageObject selectIngredient(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		if (isRowSelected(ingredient.getName())) {
			return this;
		}
		$(grid.getRowSelector(ingredient.getName())).click();
		rowShouldBeSelected(ingredient);
		return this;
	}

	public IngredientListPageObject deSelectIngredient(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		if (!isRowSelected(ingredient.getName())) {
			return this;
		}
		$(grid.getRowSelector(ingredient.getName())).click();
		rowShouldNotBeSelected(ingredient);
		return this;
	}

	public IngredientListPageObject rowShouldBeSelected(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		getRowElement(ingredient.getName()).get().shouldBe(selected());
		return this;
	}

	public IngredientListPageObject rowShouldNotBeSelected(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		getRowElement(ingredient.getName()).get().shouldNotBe(selected());
		return this;
	}

	private boolean isRowSelected(String name) {
		return getRowElement(name).map(element -> element.is(selected())).orElse(false);
	}

	private Optional<SelenideElement> getRowElement(String name) {
		return grid.getTdElementByValueOfFirstColumn(name).map(element -> Optional.of(element.parent()))
				.orElse(Optional.empty());
	}

}
