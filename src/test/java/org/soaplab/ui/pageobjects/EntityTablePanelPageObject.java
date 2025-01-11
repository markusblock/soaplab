package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;
import static org.soaplab.ui.fat.VaadinUtils.selected;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.NamedEntity;
import org.soaplab.ui.fat.VaadinGrid;
import org.soaplab.ui.fat.VaadinUtils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class EntityTablePanelPageObject {

	private final VaadinGrid grid;
	public static String COLUMN_HEADER_NAME = "Name";
	public static String COLUMN_HEADER_INCI = "Inci";
	private int columnNameIndex = 0;
	private String id;
	final List<PageObjectElement> searchFieldsToClear = new ArrayList<>();
	final List<PageObjectElement> editorsToClose = new ArrayList<>();

	public EntityTablePanelPageObject(String id) {
		this(byId(id));
		this.id = id;
	}

	private EntityTablePanelPageObject(By by) {
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
		ingredientShouldAppear(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity appears.
	 */
	public EntityTablePanelPageObject ingredientShouldAppear(String... ingredientNames) {
		// filter table otherwise the ingredient couldn't be found if it is outside the
		// displayed rows
		Arrays.asList(ingredientNames).forEach(name -> {
			searchByColumn(COLUMN_HEADER_NAME).setValue(name);
			grid.columnShouldContainAllOf(columnNameIndex, name);
			clearSearchInColumn(COLUMN_HEADER_NAME);
		});
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity doesn't appear.
	 */
	public EntityTablePanelPageObject ingredientShouldNotAppear(NamedEntity... entities) {
		ingredientShouldNotAppear(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * Filter table by entity names and check if the provided entity doesn't appear.
	 */
	public EntityTablePanelPageObject ingredientShouldNotAppear(String... ingredientNames) {
		// filter table otherwise the ingredient couldn't be found if it is outside the
		// displayed rows
		Arrays.asList(ingredientNames).forEach(name -> {
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
		ingredientShouldAppearInViewPort(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * check if the provided entity appears without filtering the table.
	 */
	public EntityTablePanelPageObject ingredientShouldAppearInViewPort(String... ingredientNames) {
		grid.columnShouldContainAllOf(columnNameIndex, Arrays.asList(ingredientNames));
		return this;
	}

	/**
	 * check if the provided entity doesn't appear without filtering the table.
	 */
	public EntityTablePanelPageObject entityShouldNotAppearInViewPort(NamedEntity... entities) {
		ingredientShouldNotAppearInViewPort(getEntityNameList(entities).toArray(new String[entities.length]));
		return this;
	}

	/**
	 * check if the provided entity doesn't appear without filtering the table.
	 */
	public EntityTablePanelPageObject ingredientShouldNotAppearInViewPort(String... ingredientNames) {
		grid.columnShouldNotContain(columnNameIndex, Arrays.asList(ingredientNames));
		return this;
	}

	public EntityTablePanelPageObject ingredientPropertyShouldBeDisplayed(Ingredient ingredient,
			String columnHeaderName, String value) {
		final int colIdx = grid.getColumnIndexByColumnHeaderText(columnHeaderName);
		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, ingredient.getName());
		grid.cellShouldContain(rowIdx, colIdx, value);
		return this;
	}

	private List<String> getEntityNameList(NamedEntity... ingredients) {
		return Arrays.asList(ingredients).stream().map(NamedEntity::getName).collect(Collectors.toList());
	}

	/**
	 * Filters table for provided ingredient and selects it. Filtered table is a
	 * side effect of this method.
	 */
	public EntityTablePanelPageObject selectIngredient(Ingredient ingredient) {
		searchByColumn(COLUMN_HEADER_NAME).setValue(ingredient.getName());
		grid.columnShouldContainAllOf(columnNameIndex, ingredient.getName());
		if (!isRowSelected(ingredient.getName())) {
			VaadinUtils.clickOnElement(grid.getRowSelector(ingredient.getName()));
			rowShouldBeSelected(ingredient);
		}
		return this;
	}

	public EntityTablePanelPageObject rowShouldBeSelected(Ingredient ingredient) {
		grid.columnShouldContainAllOf(columnNameIndex, ingredient.getName());
		getRowElement(ingredient.getName()).get().shouldBe(selected());
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
		selectIngredient(ingredient);

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

		selectIngredient(ingredient);

		final int colIdx = grid.getColumnIndexByColumnHeaderText(columnHeaderName);
		final int rowIdx = grid.getRowIndexByValue(columnNameIndex, ingredient.getName());
		final By cellSelector = grid.getCellSelector(rowIdx, colIdx);
		VaadinUtils.clickOnElement(cellSelector);
		return new PageObjectElement(cellSelector);
	}

	public PageObjectElement pressEnter(Ingredient ingredient) {
		selectIngredient(ingredient);

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
