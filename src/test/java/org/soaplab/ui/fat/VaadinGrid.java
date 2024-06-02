package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.util.CollectionUtils;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VaadinGrid {

	private final By locator;

	public Optional<SelenideElement> getTdElementByValueOfFirstColumn(String value) {
		final List<SelenideElement> vaadinGridCellContents = getVaadinGridCellContentByNameInFirstColumn(
				List.of(value));
		if (CollectionUtils.isEmpty(vaadinGridCellContents)) {
			return Optional.empty();
		}

		return Optional.of(vaadinGridCellContents.get(0));
	}

	public By getRowSelector(String rowValue) {
		return new ByChained(locator, byText(rowValue));
	}

	public void firstColumnShouldContain(String value) {
		firstColumnShouldContain(List.of(value));
	}

	public void firstColumnShouldContain(List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for first column to contain values " + StringUtils.join(values, ",")
						+ ". Values of first column " + StringUtils.join(getVaadinGridCellContentsOfFirstColumn(), ","))
				.until(driver -> firstColumnContains(values));
	}

	public void firstColumnShouldNotContain(String value) {
		firstColumnShouldNotContain(List.of(value));
	}

	public void firstColumnShouldNotContain(List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for first column to not contain values " + ". Values of first column "
						+ StringUtils.join(getVaadinGridCellContentsOfFirstColumn(), ","))
				.until(driver -> !firstColumnContains(values));
	}

	private boolean firstColumnContains(List<String> values) {
		return !CollectionUtils.isEmpty(getVaadinGridCellContentByNameInFirstColumn(values));
	}

	public List<SelenideElement> getVaadinGridCellContentByNameInFirstColumn(List<String> values) {

		WebElementCondition condition;

		if (values.size() == 1) {
			condition = Condition.text(values.get(0));
		} else if (values.size() == 2) {
			condition = Condition.or("", Condition.text(values.get(0)), Condition.text(values.get(1)));
		} else {
			final WebElementCondition[] conditions = new WebElementCondition[values.size() - 2];
			for (int i = 0; i < values.size(); i++) {
				conditions[i] = Condition.text(values.get(i + 2));
			}
			condition = Condition.or("", Condition.text(values.get(0)), Condition.text(values.get(1)), conditions);
		}

		return Selenide.$(locator).$$("vaadin-grid-cell-content").filterBy(condition).asDynamicIterable().stream()
				.map(gridCellContent -> getTdElement(gridCellContent))
				.filter(tdElement -> tdElement.has(Condition.attribute("first-column")))
				.filter(tdElement -> tdElement.is(Condition.visible)).collect(Collectors.toList());
	}

	private List<SelenideElement> getVaadinGridCellContentsOfFirstColumn() {
		return getFirstColumnTds().asDynamicIterable().stream().map(td -> getVaadinGridCellContentElement(td))
				.collect(Collectors.toList());
	}

	private ElementsCollection getFirstColumnTds() {
		return Selenide.$$(Selectors.shadowCss("tbody td", "vaadin-grid[id='entitylist.grid']"))
				.filterBy(Condition.attribute("first-column")).filterBy(Condition.visible);
	}

	private SelenideElement getTdElement(SelenideElement vaadinGridCellContent) {
		final String slotName = vaadinGridCellContent.getAttribute("slot");
		final SelenideElement tdElement = Selenide
				.$$(Selectors.shadowCss("tbody td slot", "vaadin-grid[id='entitylist.grid']"))
				.filterBy(Condition.attribute("name", slotName)).shouldHave(CollectionCondition.size(1)).get(0)
				.parent();
		return tdElement;
	}

	private SelenideElement getVaadinGridCellContentElement(SelenideElement tdElement) {
		final String slotName = tdElement.$(Selectors.byTagName("slot")).getAttribute("name");
		return $(Selectors.byCssSelector("vaadin-grid-cell-content[slot='" + slotName + "']"));
	}

}
