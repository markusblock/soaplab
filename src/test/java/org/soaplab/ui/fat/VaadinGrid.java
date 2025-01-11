package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.codeborne.selenide.Condition;
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

	public By getRowSelector(int rowIdx) {
		return new ByChained(locator, Selectors.shadowDeepCss("table tbody tr:nth-of-type(%s)".formatted(rowIdx)));
	}

	public By getCellSelector(String rowValue, int colIdx) {
		return new ByChained(locator, byText(rowValue), Selectors.byXpath("(th)[%s]".formatted(colIdx)));
	}

	public By getCellSelector(int rowIdx, int colIdx) {
		return getTdLocator(rowIdx, colIdx);
	}

	public void cellShouldContain(int rowIdx, int colIdx, String value) {
		final SelenideElement tdElement = getTdElement(rowIdx, colIdx);
		Selenide.$(getGridCellContentLocator(tdElement)).shouldHave(Condition.text(value));
	}

	public void columnShouldContainAllOf(int colIdx, String value) {
		columnShouldContainAllOf(colIdx, List.of(value));
	}

	public void columnShouldContainAllOf(int colIdx, List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for column %s to contain values '%s'. Values of column are '%s'".formatted(colIdx,
						StringUtils.join(values, ","), getTextOfTds(colIdx)))
				.until(driver -> columnContainsAllOf(colIdx, values));
	}

	/**
	 * Returns the index of the column specified by the header text. Search is done
	 * case insensitive. index is 1-based
	 *
	 * @param headerText
	 * @return
	 */
	public int getColumnIndexByColumnHeaderText(String headerText) {
		final List<SelenideElement> ths = Selenide.$(locator).$$(Selectors.shadowDeepCss("table thead th"))
				.asFixedIterable().stream().toList();
		for (int i = 0; i < ths.size(); i++) {
			final SelenideElement selenideElement = ths.get(i);
			if (headerText.equalsIgnoreCase(selenideElement.getText())) {
				return i + 1;
			}
		}
		throw new RuntimeException("Couldn't find column with header text %s".formatted(headerText));
	}

	public String getColumnIdByColumnHeaderText(String headerText) {
		final List<SelenideElement> ths = Selenide.$(locator).$$(Selectors.shadowDeepCss("table thead th"))
				.asFixedIterable().stream().toList();
		for (int i = 0; i < ths.size(); i++) {
			final SelenideElement selenideElement = ths.get(i);
			if (headerText.equalsIgnoreCase(selenideElement.getText())) {
				final SelenideElement gridCellContentElement = getGridCellContentElement(selenideElement);
				final String attr = gridCellContentElement.$("vaadin-text-field").attr("id");
				return attr;
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
		for (int i = 0; i < tdsOfColumn.size(); i++) {
			final SelenideElement tdOfColumn = tdsOfColumn.get(i);
			final SelenideElement gridCellContentElement = getGridCellContentElement(tdOfColumn);
			// get text from content directly if editor is closed
			final String text = gridCellContentElement.getText();
			if (rowValue.equalsIgnoreCase(text)) {
				return i + 1;
			} else if (ObjectUtils.isEmpty(text)) {
				// get text from textfield if editor is opened
				final String text2 = gridCellContentElement.$("vaadin-text-field").getValue();
				if (text2.equalsIgnoreCase(rowValue)) {
					return i + 1;
				}
			}
		}
		throw new RuntimeException("Couldn't find row with value %s in column %s".formatted(rowValue, columnIndex));
	}

	public By getEditorLocator(int rowIndex, int columnIndex) {
		final SelenideElement tdElement = getTdElement(rowIndex, columnIndex);
		return new ByChained(getGridCellContentLocator(tdElement), By.cssSelector("vaadin-text-field"));
	}

	private SelenideElement getTdElement(int rowIndex, int columnIndex) {
		return $(locator).$(getTdLocator(rowIndex, columnIndex));
	}

	private By getTdLocator(int rowIndex, int columnIndex) {
		return Selectors
				.shadowDeepCss("table tbody tr:nth-of-type(%s) td:nth-of-type(%s)".formatted(rowIndex, columnIndex));
	}

	private SelenideElement getGridCellContentElement(SelenideElement tdElement) {
		return Selenide.$(getGridCellContentLocator(tdElement));
	}

	private By getGridCellContentLocator(SelenideElement tdElement) {
		final String slotName = tdElement.$("slot").attr("name");
		return new ByChained(locator, By.cssSelector("vaadin-grid-cell-content[slot='%s']".formatted(slotName)));
	}

	public void columnShouldNotContain(int colIdx, String value) {
		columnShouldNotContain(colIdx, List.of(value));
	}

	public void columnShouldNotContain(int colIdx, List<String> values) {
		Selenide.Wait()
				.withMessage("Waiting for column %s to not contain values '%s'. Values of first column are '%s'"
						.formatted(colIdx, values, getTextOfTds(colIdx)))
				.until(driver -> !columnContainsAllOf(colIdx, values));
	}

	private boolean columnContainsAllOf(int columnIndex, List<String> values) {
		final List<SelenideElement> tdsOfColumnFilteredByValue = getTdsOfColumnFilteredByValue(columnIndex, values);
		return !CollectionUtils.isEmpty(tdsOfColumnFilteredByValue)
				&& tdsOfColumnFilteredByValue.size() == values.size();
	}

	public Optional<SelenideElement> getTdElementByValueOfColumn(int columnIndex, String value) {
		final List<SelenideElement> vaadinGridCellContents = getTdsOfColumnFilteredByValue(columnIndex, List.of(value));
		if (CollectionUtils.isEmpty(vaadinGridCellContents)) {
			return Optional.empty();
		}

		return Optional.of(vaadinGridCellContents.get(0));
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
		final List<SelenideElement> trs = Selenide.$(locator).$$(Selectors.shadowDeepCss("table tbody tr"))
				.asDynamicIterable().stream().toList();
		for (final SelenideElement selenideElement : trs) {
			final List<SelenideElement> tdsOfRow = selenideElement.$$("td").asFixedIterable().stream().toList();
			Assert.isTrue(columnIndex < tdsOfRow.size(), "column index must be <=" + tdsOfRow.size());
			// tdsOfRow 0-based, columnIndex 1-based
			final SelenideElement cell = tdsOfRow.get(columnIndex - 1);
			tdsOfColumn.add(cell);
		}
		return tdsOfColumn;
	}

	public void filter(int colIdx, String string) {
		final List<SelenideElement> ths = Selenide.$(locator).$$(Selectors.shadowDeepCss("table thead th"))
				.asFixedIterable().stream().toList();
		final SelenideElement selenideElement = ths.get(colIdx);
		selenideElement.$(Selectors.shadowDeepCss("vaadin-text-field input")).append(string);
	}

	private String getTextOfTds(int colIdx) {
		return StringUtils.join(getTdsOfColumn(colIdx).stream().map(element -> element.getText()).toList(), ",");
	}
}
