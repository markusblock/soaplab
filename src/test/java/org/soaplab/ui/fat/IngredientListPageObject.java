package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byId;
import static org.soaplab.ui.fat.VaadinUtils.selected;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.soaplab.domain.Ingredient;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

public class IngredientListPageObject {

	private VaadinGrid grid;

	public IngredientListPageObject() {
		grid = new VaadinGrid(byId("entitylist.grid"));
	}

	public PageObjectElement search() {
		return new PageObjectElement(byId("entitylist.search"));
	}

	public PageObjectElement buttonSearchReset() {
		return new PageObjectElement(
				Selectors.shadowCss("div[id='clearButton']", "vaadin-text-field[id='entitylist.search']"));
	}

	public PageObjectElement buttonAdd() {
		return new PageObjectElement(byId("entitylist.add"));
	}

	public PageObjectElement buttonRemove() {
		return new PageObjectElement(byId("entitylist.remove"));
	}

	public IngredientListPageObject ingredientShouldAppear(Ingredient... ingredients) {
		grid.firstColumnShouldContain(getIngredientNameList(ingredients));
		return this;
	}

	public IngredientListPageObject ingredientShouldAppear(String... ingredientNames) {
		grid.firstColumnShouldContain(Arrays.asList(ingredientNames));
		return this;
	}

	public IngredientListPageObject ingredientShouldNotAppear(Ingredient... ingredients) {
		grid.firstColumnShouldNotContain(getIngredientNameList(ingredients));
		return this;
	}

	public IngredientListPageObject ingredientShouldNotAppear(String... ingredientNames) {
		grid.firstColumnShouldNotContain(Arrays.asList(ingredientNames));
		return this;
	}

	private List<String> getIngredientNameList(Ingredient... ingredients) {
		return Arrays.asList(ingredients).stream().map(Ingredient::getName).collect(Collectors.toList());
	}

	public IngredientListPageObject selectIngredient(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		if (isRowSelected(ingredient.getName())) {
			return this;
		}
		VaadinUtils.clickOnElement(grid.getRowSelector(ingredient.getName()));
		rowShouldBeSelected(ingredient);
		return this;
	}

	public IngredientListPageObject deSelectIngredient(Ingredient ingredient) {
		grid.firstColumnShouldContain(ingredient.getName());
		if (!isRowSelected(ingredient.getName())) {
			return this;
		}
		VaadinUtils.clickOnElement(grid.getRowSelector(ingredient.getName()));
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

	public void triggerReload() {
		search().setValue("a");
		buttonSearchReset().click();
	}

	public void reset() {
		// clear search
		if (buttonSearchReset().isVisible()) {
			buttonSearchReset().click();
		}
	}

}
