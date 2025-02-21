package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.soaplab.domain.NamedEntity;
import org.soaplab.ui.VaadinGrid;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

public class EntityTablePanelPageObject {

	private final VaadinGrid grid;
	public static String COLUMN_HEADERNAME_NAME = "Name";
	public static String COLUMN_HEADERNAME_INCI = "INCI";
	private final EntityTableContext context;

	public EntityTablePanelPageObject(String id) {
		this(byId(id));
	}

	private EntityTablePanelPageObject(By by) {

		Selenide.$(by).shouldBe(Condition.visible);

		grid = new VaadinGrid(by);

		context = new EntityTableContext();
		context.setColumnNameIndex(grid.getColumnIndexByColumnHeaderText(COLUMN_HEADERNAME_NAME));
	}

	protected void addEditorTypeForColumn(String columnHeaderName, String editorType) {
		context.addEditorTypeForColumn(columnHeaderName, editorType);
	}

	public PageObjectElement getColumnFilter(String columnHeaderName) {
		final String id = grid.getColumnIdByColumnHeaderText(columnHeaderName);
		final PageObjectElement searchField = new PageObjectElement(byId(id));
		context.addFilterTextFieldToClear(searchField);
		return searchField;
	}

	public void clearColumnFilter(String columnHeaderName) {
		if (grid.isFilterable()) {
			final String id = grid.getColumnIdByColumnHeaderText(columnHeaderName);
			new PageObjectElement(byId(id)).setValue((String) null).shouldBeEmpty();
		}
	}

	public void filterGrid(String filterValue, String columnHeaderName) {
		if (grid.isFilterable()) {
			getColumnFilter(columnHeaderName).setValue(filterValue);
		}
	}

	/**
	 * Filter table by entity names and check if the provided entity appears.
	 */
	public EntityTablePanelPageObject entityShouldAppear(NamedEntity... entities) {
		entityShouldAppear(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity appears.
	 */
	public EntityTablePanelPageObject entityShouldAppear(String... entityNames) {
		// filter table otherwise the ingredient couldn't be found if it is outside the
		// displayed rows
		Arrays.asList(entityNames).forEach(name -> {
			filterGrid(name, COLUMN_HEADERNAME_NAME);
			grid.columnShouldContainExactly(context.getColumnNameIndex(), name);
			clearColumnFilter(COLUMN_HEADERNAME_NAME);
		});
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity doesn't appear.
	 */
	public EntityTablePanelPageObject entityShouldNotAppear(NamedEntity... entities) {
		entityShouldNotAppear(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity doesn't appear.
	 */
	public EntityTablePanelPageObject entityShouldNotAppear(String... entityNames) {
		// filter table otherwise the ingredient couldn't be found if it is outside the
		// displayed rows
		Arrays.asList(entityNames).forEach(name -> {
			filterGrid(name, COLUMN_HEADERNAME_NAME);
			grid.columnShouldNotContainAnyOf(context.getColumnNameIndex(), name);
			clearColumnFilter(COLUMN_HEADERNAME_NAME);
		});
		return this;
	}

	/**
	 * check if the provided entity appears without filtering the table.
	 */
	public EntityTablePanelPageObject entityShouldAppearInViewPort(NamedEntity... entities) {
		entityShouldAppearInViewPort(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * check if the provided entity appears without filtering the table.
	 */
	public EntityTablePanelPageObject entityShouldAppearInViewPort(String... entityNames) {
		grid.columnShouldContainExactly(context.getColumnNameIndex(), Arrays.asList(entityNames));
		return this;
	}

	/**
	 * check if the provided entity doesn't appear without filtering the table.
	 */
	public EntityTablePanelPageObject entityShouldNotAppearInViewPort(NamedEntity... entities) {
		entityShouldNotAppearInViewPort(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * check if the provided entity doesn't appear without filtering the table.
	 */
	public EntityTablePanelPageObject entityShouldNotAppearInViewPort(String... entityNames) {
		grid.columnShouldNotContainAnyOf(context.getColumnNameIndex(), Arrays.asList(entityNames));
		return this;
	}

	private List<String> getEntityNameList(NamedEntity... ingredients) {
		return Arrays.asList(ingredients).stream().map(NamedEntity::getName).collect(Collectors.toList());
	}

	public void reset() {
		context.getFilterTextFieldsToClear().forEach(searchField -> searchField.setValue(""));
		context.getEditorsToClose().stream().filter(editor -> editor.isVisible())
				.forEach(editor -> editor.pressEscape());
	}

	/**
	 * Returns a row object that represents the provided entity. In order to find
	 * the row the table is filtered first. The filter is kept after the method
	 * returns.
	 */
	public RowGridObject row(NamedEntity entity) {
		filterGrid(entity.getName(), COLUMN_HEADERNAME_NAME);

		final int rowIdx = grid.getRowIndexByValue(context.getColumnNameIndex(), entity.getName());
		return new RowGridObject(grid, context, rowIdx);
	}

	public RowGridObject row(String headerText, String cellValue) {
		filterGrid(cellValue, headerText);

		final int colIdx = grid.getColumnIndexByColumnHeaderText(headerText);
		final int rowIdx = grid.getRowIndexByValue(colIdx, cellValue);
		return new RowGridObject(grid, context, rowIdx);
	}

	public RowGridObject row(int rowIdx) {
		return new RowGridObject(grid, context, rowIdx);
	}

}
