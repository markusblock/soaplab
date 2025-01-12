package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;
import static org.soaplab.ui.VaadinUtils.selected;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.NamedEntity;
import org.soaplab.ui.VaadinGrid;
import org.soaplab.ui.VaadinUtils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class EntityTablePanelPageObject {

	private final VaadinGrid grid;
	public static String COLUMN_HEADER_NAME = "Name";
	public static String COLUMN_HEADER_INCI = "Inci";
	private int columnNameIndex = 0;
	final List<PageObjectElement> searchFieldsToClear = new ArrayList<>();
	final List<PageObjectElement> editorsToClose = new ArrayList<>();

	public EntityTablePanelPageObject(String id) {
		this(byId(id));
	}

	private EntityTablePanelPageObject(By by) {

		Selenide.$(by).shouldBe(Condition.visible);

		grid = new VaadinGrid(by);
		columnNameIndex = grid.getColumnIndexByColumnHeaderText(COLUMN_HEADER_NAME);
	}

	public PageObjectElement searchByColumn(String columnHeaderName) {
		final String id = grid.getColumnIdByColumnHeaderText(columnHeaderName);
		final PageObjectElement searchField = new PageObjectElement(byId(id));
		searchFieldsToClear.add(searchField);
		return searchField;
	}

	public void clearSearchInColumn(String columnHeaderName) {
		final String id = grid.getColumnIdByColumnHeaderText(columnHeaderName);
		new PageObjectElement(byId(id)).setValue((String) null).shouldBeEmpty();
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
			searchByColumn(COLUMN_HEADER_NAME).setValue(name);
			grid.columnShouldContainAllOf(columnNameIndex, name);
			clearSearchInColumn(COLUMN_HEADER_NAME);
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
			searchByColumn(COLUMN_HEADER_NAME).setValue(name);
			grid.columnShouldNotContain(columnNameIndex, name);
			clearSearchInColumn(COLUMN_HEADER_NAME);
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
		grid.columnShouldContainAllOf(columnNameIndex, Arrays.asList(entityNames));
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
		grid.columnShouldNotContain(columnNameIndex, Arrays.asList(entityNames));
		return this;
	}

	public EntityTablePanelPageObject entityPropertyShouldBeDisplayed(NamedEntity namedEntity, String columnHeaderName,
			String value) {
		final int colIdx = grid.getColumnIndexByColumnHeaderText(columnHeaderName);
		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, namedEntity.getName());
		grid.cellShouldContain(rowIdx, colIdx, value);
		return this;
	}

	private List<String> getEntityNameList(NamedEntity... ingredients) {
		return Arrays.asList(ingredients).stream().map(NamedEntity::getName).collect(Collectors.toList());
	}

	/**
	 * Filters table for provided entity and selects it. Filtered table is a side
	 * effect of this method.
	 */
	public EntityTablePanelPageObject selectEntity(NamedEntity entity) {
		searchByColumn(COLUMN_HEADER_NAME).setValue(entity.getName());
		grid.columnShouldContainAllOf(columnNameIndex, entity.getName());
		if (!isRowSelected(entity.getName())) {
			VaadinUtils.clickOnElement(grid.getRowSelector(entity.getName()));
			rowShouldBeSelected(entity);
		}
		return this;
	}

	public EntityTablePanelPageObject rowShouldBeSelected(NamedEntity entity) {
		grid.columnShouldContainAllOf(columnNameIndex, entity.getName());
		getRowElement(entity.getName()).get().shouldBe(selected());
		return this;
	}

	public EntityTablePanelPageObject rowShouldNotBeSelected(Ingredient ingredient) {
		grid.columnShouldContainAllOf(columnNameIndex, ingredient.getName());
		getRowElement(ingredient.getName()).get().shouldNotBe(selected());
		return this;
	}

	private boolean isRowSelected(String name) {
		return getRowElement(name).map(element -> element.is(selected())).orElse(false);
	}

	private Optional<SelenideElement> getRowElement(String name) {
		return grid.getTdElementByValueOfColumn(columnNameIndex, name).map(element -> Optional.of(element.parent()))
				.orElse(Optional.empty());
	}

	public void reset() {
		searchFieldsToClear.forEach(searchField -> searchField.setValue(""));
		editorsToClose.stream().filter(editor -> editor.isVisible()).forEach(editor -> editor.pressEscape());
	}

	public PageObjectElement doubleClick(Ingredient ingredient, String columnHeaderName) {
		selectEntity(ingredient);

		final int colIdx = grid.getColumnIndexByColumnHeaderText(columnHeaderName);
		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, ingredient.getName());

		VaadinUtils.doubleClickOnElement(grid.getCellSelector(rowIdx, colIdx));

		final By editorLocator = grid.getEditorLocator(rowIdx, colIdx);
		Selenide.$(editorLocator).shouldBe(Condition.visible);
		final PageObjectElement editor = new PageObjectElement(editorLocator);
		editorsToClose.add(editor);
		return editor;
	}

	public PageObjectElement click(Ingredient ingredient, String columnHeaderName) {

		// TODO differ editmode

		selectEntity(ingredient);

		final int colIdx = grid.getColumnIndexByColumnHeaderText(columnHeaderName);
		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, ingredient.getName());
		final By cellSelector = grid.getCellSelector(rowIdx, colIdx);
		VaadinUtils.clickOnElement(cellSelector);
		return new PageObjectElement(cellSelector);
	}

	public PageObjectElement pressEnter(Ingredient ingredient) {
		selectEntity(ingredient);

		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, ingredient.getName());

		final By rowSelector = grid.getRowSelector(rowIdx);
		Selenide.$(rowSelector).pressEnter();
		final By editorLocator = grid.getEditorLocator(rowIdx, columnNameIndex);
		Selenide.$(editorLocator).shouldBe(Condition.visible);
		final PageObjectElement editor = new PageObjectElement(editorLocator);
		editorsToClose.add(editor);
		return editor;
	}
}
