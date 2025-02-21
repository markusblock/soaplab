package org.soaplab.ui.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

class EntityTableContext {
	@Getter
	@Setter int columnNameIndex = 0;
	private final List<PageObjectElement> filterTextFieldsToClear = new ArrayList<>();
	private final List<PageObjectElement> editorsToClose = new ArrayList<>();
	private final Map<String, String> editorTypeMapping = new HashMap<>();

	/**
	 * Editor type default is 'vaadin-text-field' for all not specified columns. If
	 * another type is configured it needs to be specified here, e.g. for
	 * 'vaadin-combo-box'. It is used to construct the editor locator.
	 *
	 * @param columnHeaderName
	 * @param editorType
	 */
	public void addEditorTypeForColumn(String columnHeaderName, String editorType) {
		editorTypeMapping.put(columnHeaderName, editorType);
	}

	public String getEditorTypeForColumn(String columnHeaderName) {
		return editorTypeMapping.getOrDefault(columnHeaderName, "vaadin-text-field");
	}

	public void addFilterTextFieldToClear(PageObjectElement filterTextField) {
		filterTextFieldsToClear.add(filterTextField);
	}

	public List<PageObjectElement> getFilterTextFieldsToClear() {
		return filterTextFieldsToClear;
	}

	public void addEditorToClose(PageObjectElement editor) {
		editorsToClose.add(editor);
	}

	public List<PageObjectElement> getEditorsToClose() {
		return editorsToClose;
	}
}