package org.soaplab.ui.fat;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.soaplab.ui.fat.VaadinUtils.selected;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.soaplab.domain.Ingredient;
import org.springframework.util.CollectionUtils;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class IngredientListPageObject {

	By ingredientlistGridSelector = byId("ingredientlist.grid");

	public IngredientListPageObject ingredientShouldAppear(String name) {
		nameColumnShouldContain(name);
		return this;
	}

	public IngredientListPageObject selectIngredient(Ingredient ingredient) {
		nameColumnShouldContain(ingredient.getName());
		if (isRowSelected(ingredient.getName())) {
			return this;
		}
		$(getRowSelector(ingredient.getName())).click();
		return this;
	}

	private boolean isRowSelected(String name) {
		return getRowElement(name).map(element -> element.is(selected())).orElse(false);
	}

	private Optional<SelenideElement> getRowElement(String name) {
		return getTdElementByValueOfFirstColumn(name).map(element -> Optional.of(element.parent()))
				.orElse(Optional.empty());
	}

	private Optional<SelenideElement> getTdElementByValueOfFirstColumn(String name) {
		List<SelenideElement> vaadinGridCellContents = getVaadinGridCellContentByNameInFirstColumn(name);
		if (CollectionUtils.isEmpty(vaadinGridCellContents)) {
			return Optional.empty();
		}

		return Optional.of(getTdElement(vaadinGridCellContents.get(0)));
	}

	private void nameColumnShouldContain(String name) {
		Selenide.Wait().until(driver -> nameColumnContains(name));
	}

	private boolean nameColumnContains(String name) {
		return !CollectionUtils.isEmpty(getVaadinGridCellContentByNameInFirstColumn(name));
	}

	private List<SelenideElement> getVaadinGridCellContentByNameInFirstColumn(String name) {
		return getFirstColumnTds().stream().map(element -> getVaadinGridCellContentElement(element))
				.filter(element -> element.has(Condition.text(name))).collect(Collectors.toList());
	}

	private ElementsCollection getFirstColumnTds() {
		return Selenide.$$(Selectors.shadowCss("tbody td", "vaadin-grid[id='ingredientlist.grid']"))
				.filterBy(Condition.attribute("first-column"));
	}

	private SelenideElement getTdElement(SelenideElement vaadinGridCellContent) {
		String slotName = vaadinGridCellContent.getAttribute("slot");
		SelenideElement tdElement = Selenide
				.$$(Selectors.shadowCss("tbody td slot", "vaadin-grid[id='ingredientlist.grid']"))
				.filterBy(Condition.attribute("name", slotName)).shouldHave(CollectionCondition.size(1)).get(0)
				.parent();
		return tdElement;
	}

	private SelenideElement getVaadinGridCellContentElement(SelenideElement tdElement) {
		String slotName = tdElement.$(Selectors.byTagName("slot")).getAttribute("name");
		return $(Selectors.byCssSelector("vaadin-grid-cell-content[slot='" + slotName + "']"));
	}

	private By getRowSelector(String rowValue) {
		return new ByChained(ingredientlistGridSelector, byText(rowValue));
	}

	public IngredientListPageObject ingredientShouldNotAppear(String name) {
		$(ingredientlistGridSelector).shouldNotHave(text(name));
		return this;
	}

}
