package org.soaplab.ui;

import static com.codeborne.selenide.Selectors.byText;
import static org.soaplab.ui.VaadinUtils.classAttributeContainingClass;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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

	private final By locator;

	public By getRowSelector(String rowValue) {
		return new ByChained(locator, byText(rowValue));
	}

	public void cellShouldContain(int rowIdx, int colIdx, String value) {
		final SelenideElement tdElement = getTdElement(rowIdx, colIdx);
		Selenide.$(getGridCellContentLocator(tdElement)).shouldHave(Condition.text(value));
	}

	public void columnShouldContainExactly(int colIdx, String value) {
		columnShouldContainExactly(colIdx, List.of(value));
	}

	public boolean isFilterable() {
		return Selenide.$(locator).has(classAttributeContainingClass("filterable"));
	}

	public void columnShouldContainExactly(int colIdx, List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for column %s to contain values '%s'. Values of column are '%s'".formatted(colIdx,
						StringUtils.join(values, ","), getTextOfTds(colIdx)))
				.until(driver -> columnContainsExactly(colIdx, values));
	}

	/**
	 * Returns the index of the column specified by the header text. Search is done
	 * case insensitive. index is 1-based
	 *
	 * @param headerText
	 * @return
	 */
	public int getColumnIndexByColumnHeaderText(String headerText) {
		final ElementsCollection trs = findElementsInGridShadowRoot(Selectors.byCssSelector("table thead tr"));
		for (int trIndex = 0; trIndex < trs.size(); trIndex++) {
			final SelenideElement tr = trs.get(trIndex);
			final ElementsCollection ths = tr.$$("th");
			int columnCounter = 1;
			for (int thIndex = 0; thIndex < ths.size(); thIndex++) {
				final SelenideElement th = ths.get(thIndex);
				if (headerText.equalsIgnoreCase(th.getText())) {
					return columnCounter;
				}
				columnCounter++;
			}
		}
		throw new RuntimeException("Couldn't find column with header text %s".formatted(headerText));
	}

	public String getColumnIdByColumnHeaderText(String headerText) {
		final ElementsCollection trs = findElementsInGridShadowRoot(Selectors.byCssSelector("table thead tr"));
		for (int trIndex = 0; trIndex < trs.size(); trIndex++) {
			final SelenideElement tr = trs.get(trIndex);
			final ElementsCollection ths = tr.$$("th");
			for (int thIndex = 0; thIndex < ths.size(); thIndex++) {
				final SelenideElement th = ths.get(thIndex);
				if (headerText.equalsIgnoreCase(th.getText())) {
					final SelenideElement gridCellContentElement = getGridCellContentElement(th);
					final String attr = gridCellContentElement.$("vaadin-text-field").attr("id");
					return attr;
				}
			}
		}

		throw new RuntimeException("Couldn't find column with header text %s".formatted(headerText));
	}

	/**
	 * Returns the index of the row specified by the provided value. Search is done
	 * case insensitive. index is 1-based
	 *
	 * @param rowValue
	 * @param columnIndex 1-based column index where to look for the provided value
	 * @return index of row 1-based
	 */
	public int getRowIndexByValue(int columnIndex, String rowValue) {
		final List<SelenideElement> tdsOfColumn = getTdsOfColumn(columnIndex);
		int rowCounter = 1;
		for (int i = 0; i < tdsOfColumn.size(); i++) {
			final SelenideElement tdOfColumn = tdsOfColumn.get(i);
			final SelenideElement gridCellContentElement = getGridCellContentElement(tdOfColumn);
			final boolean displayed = gridCellContentElement.isDisplayed();
			if (displayed) {
				// get text from content directly if editor is closed
				final String text = gridCellContentElement.getText();
				if (rowValue.equalsIgnoreCase(text)) {
					return rowCounter;
				} else if (ObjectUtils.isEmpty(text)) {
					// get text from textfield if editor is opened
					final String text2 = gridCellContentElement.$("vaadin-text-field").getValue();
					if (text2.equalsIgnoreCase(rowValue)) {
						return rowCounter;
					}
				}
				// count only visible rows
				rowCounter++;
			} else {
				// strange but there are fake row contained
				// do not count this row
			}
		}
		throw new RuntimeException("Couldn't find row with value %s in column %s".formatted(rowValue, columnIndex));
	}

	public By getEditorLocator(int rowIndex, int columnIndex, String editorType) {
		final SelenideElement tdElement = getTdElement(rowIndex, columnIndex);
		return new ByChained(getGridCellContentLocator(tdElement), By.cssSelector(editorType));
	}

	public void columnShouldNotContainAnyOf(int colIdx, String value) {
		columnShouldNotContainAnyOf(colIdx, List.of(value));
	}

	public void columnShouldNotContainAnyOf(int colIdx, List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for column %s to not contain values '%s'. Values of first column are '%s'"
						.formatted(colIdx, values, getTextOfTds(colIdx)))
				.until(driver -> columnContainsNoneOf(colIdx, values));
	}

	/**
	 * Returns all cells of a column (specified by index) that contains one of the
	 * provided values. If more than one row contains the same value, the result
	 * contains multiple cells of the same value. E.g. if the column doesn't contain
	 * unique attributes.
	 *
	 * @param columnIndex look only in column with index
	 * @param values      filter rows by values, each
	 * @return List of cells {@link SelenideElement}
	 */
	public List<SelenideElement> getTdsOfColumnFilteredByValue(int columnIndex, List<String> values) {
		final List<SelenideElement> filteredCellsOfColumn = new ArrayList<>();
		final List<SelenideElement> tdsOfColumn = getTdsOfColumn(columnIndex);
		for (final SelenideElement tdOfRow : tdsOfColumn) {
			if (values.contains(tdOfRow.getText())) {
				filteredCellsOfColumn.add(tdOfRow);
			}
		}
		return filteredCellsOfColumn;
	}

	/**
	 * Returns all cells of a column (specified by index).
	 *
	 * @param columnIndex look only in column with index. Index is 1-based
	 * @return List of cells {@link SelenideElement}
	 */
	public List<SelenideElement> getTdsOfColumn(int columnIndex) {
		Assert.isTrue(columnIndex > 0, "column index must be >0");
		final List<SelenideElement> tdsOfColumn = new ArrayList<>();
		final ElementsCollection trs = findElementsInGridShadowRoot(Selectors.byCssSelector("table tbody#items tr"));
		for (final SelenideElement selenideElement : trs) {
			final List<SelenideElement> tdsOfRow = selenideElement.$$("td").asFixedIterable().stream().toList();
			// tdsOfRow 0-based, columnIndex 1-based
			final SelenideElement cell = tdsOfRow.get(columnIndex - 1);
			tdsOfColumn.add(cell);
		}
		return tdsOfColumn;
	}

	public SelenideElement getTdElement(int rowIdx, int colIdx) {
		return Selenide.$(getTdWebElement(rowIdx, colIdx));
	}

	public WebElement getTdWebElement(int rowIdx, int colIdx) {
		return findWebElementInGridShadowRoot(
				By.cssSelector("table tbody#items tr:nth-child(%s) td:nth-child(%s)".formatted(rowIdx, colIdx)));
	}

	public SelenideElement getTrElement(int rowIdx) {
		return Selenide.$(getTrWebElement(rowIdx));
	}

	public WebElement getTrWebElement(int rowIdx) {
		return findWebElementInGridShadowRoot(By.cssSelector("table tbody#items tr:nth-child(%s)".formatted(rowIdx)));
	}

	private ElementsCollection findElementsInGridShadowRoot(By cssSelector) {
		return Selenide.$$(findWebElementsInGridShadowRoot(cssSelector));
	}

	private SelenideElement findElementInGridShadowRoot(By cssSelector) {
		return Selenide.$(findWebElementInGridShadowRoot(cssSelector));
	}

	private List<WebElement> findWebElementsInGridShadowRoot(By cssSelector) {
		return getShadowRoot().findElements(cssSelector);
	}

	private WebElement findWebElementInGridShadowRoot(By cssSelector) {
		return getShadowRoot().findElement(cssSelector);
	}

	private SearchContext getShadowRoot() {
		return Selenide.$(locator).getWrappedElement().getShadowRoot();
	}

	public SelenideElement getGridCellContentElement(SelenideElement tdElement) {
		return Selenide.$(getGridCellContentLocator(tdElement));
	}

	private By getGridCellContentLocator(SelenideElement tdElement) {
		final String slotName = tdElement.$("slot").attr("name");
		return new ByChained(locator, By.cssSelector("vaadin-grid-cell-content[slot='%s']".formatted(slotName)));
	}

	private boolean columnContainsExactly(int columnIndex, List<String> values) {
		final List<SelenideElement> tdsOfColumnFilteredByValue = getTdsOfColumnFilteredByValue(columnIndex, values);
		return !CollectionUtils.isEmpty(tdsOfColumnFilteredByValue)
				&& tdsOfColumnFilteredByValue.size() == values.size();
	}

	private boolean columnContainsNoneOf(int columnIndex, List<String> values) {
		final List<SelenideElement> tdsOfColumnFilteredByValue = getTdsOfColumnFilteredByValue(columnIndex, values);
		return CollectionUtils.isEmpty(tdsOfColumnFilteredByValue);
	}

	private String getTextOfTds(int colIdx) {
		return StringUtils.join(getTdsOfColumn(colIdx).stream().map(element -> element.getText()).toList(), ",");
	}
}
