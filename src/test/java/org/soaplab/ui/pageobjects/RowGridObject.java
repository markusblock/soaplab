package org.soaplab.ui.pageobjects;

import static org.soaplab.ui.VaadinUtils.selected;
import static org.soaplab.ui.pageobjects.EntityTablePanelPageObject.COLUMN_HEADERNAME_NAME;

import org.openqa.selenium.By;
import org.soaplab.ui.VaadinGrid;
import org.soaplab.ui.VaadinUtils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class RowGridObject {

	private final VaadinGrid grid;
	private final int rowIdx;
	private final EntityTableContext context;

	public RowGridObject(VaadinGrid grid, EntityTableContext context, int rowIdx) {
		this.grid = grid;
		this.context = context;
		this.rowIdx = rowIdx;
	}

	public SelenideElement cellByHeader(String headerText) {
		final int colIdx = grid.getColumnIndexByColumnHeaderText(headerText);
		return grid.getTdElement(rowIdx, colIdx);
	}

	public SelenideElement cellByIndex(int colIdx) {
		return grid.getTdElement(rowIdx, colIdx);
	}

	public RowGridObject click() {
		return click(context.getColumnNameIndex());
	}

	public RowGridObject click(String headerText) {
		final int colIdx = grid.getColumnIndexByColumnHeaderText(headerText);
		return click(colIdx);
	}

	public RowGridObject click(int colIdx) {
		final SelenideElement tdElement = cellByIndex(colIdx);
		VaadinUtils.clickOnElement(tdElement);
		return this;
	}

	public RowGridObject select() {
		if (!isSelected()) {
			click();
			shouldBeSelected();
		}
		return this;
	}

	public boolean isSelected() {
		return grid.getTrElement(rowIdx).is(selected());
	}

	public void shouldBeSelected() {
		grid.getTrElement(rowIdx).shouldBe(selected());
	}

	public PageObjectElement doubleClick() {
		return doubleClick(COLUMN_HEADERNAME_NAME);
	}

	public PageObjectElement doubleClick(String headerText) {

		final int colIdx = grid.getColumnIndexByColumnHeaderText(headerText);

		final SelenideElement tdElement = cellByHeader(headerText);
		final SelenideElement gridCellContentElement = grid.getGridCellContentElement(tdElement);

		VaadinUtils.doubleClickOnElement(gridCellContentElement);
		gridCellContentElement.shouldBe(Condition.visible);
		// enable editor if grid in details panel
		VaadinUtils.clickOnElement(gridCellContentElement);

		return getEditor(colIdx, headerText);
	}

	public PageObjectElement pressEnter() {
		cellByIndex(context.columnNameIndex).pressEnter();
		return getEditor(context.getColumnNameIndex(), COLUMN_HEADERNAME_NAME);
	}

	public PageObjectElement getEditor() {
		return getEditor(1, COLUMN_HEADERNAME_NAME);
	}

	public PageObjectElement getEditor(String headerText) {
		final int colIdx = grid.getColumnIndexByColumnHeaderText(headerText);
		return getEditor(colIdx, headerText);
	}

	public PageObjectElement getEditor(int colIdx, String headerText) {
		final By editorLocator = grid.getEditorLocator(rowIdx, colIdx, context.getEditorTypeForColumn(headerText));
		Selenide.$(editorLocator).shouldBe(Condition.visible);
		final PageObjectElement editor = new PageObjectElement(editorLocator);
		context.addEditorToClose(editor);
		return editor;
	}

}