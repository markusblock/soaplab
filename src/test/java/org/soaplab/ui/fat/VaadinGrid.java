package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.util.CollectionUtils;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VaadinGrid {

	private By locator;

	public Optional<SelenideElement> getTdElementByValueOfFirstColumn(String value) {
		List<SelenideElement> vaadinGridCellContents = getVaadinGridCellContentByNameInFirstColumn(value);
		if (CollectionUtils.isEmpty(vaadinGridCellContents)) {
			return Optional.empty();
		}

		return Optional.of(getTdElement(vaadinGridCellContents.get(0)));
	}

	public By getRowSelector(String rowValue) {
		return new ByChained(locator, byText(rowValue));
	}

	public void firstColumnShouldContain(String value) {
		Selenide.Wait().until(driver -> firstColumnContains(value));
	}

	public void firstColumnShouldNotContain(String value) {
		Selenide.Wait().until(driver -> !firstColumnContains(value));
	}

	private boolean firstColumnContains(String value) {
		return !CollectionUtils.isEmpty(getVaadinGridCellContentByNameInFirstColumn(value));
	}

	public List<SelenideElement> getVaadinGridCellContentByNameInFirstColumn(String name) {
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

}
