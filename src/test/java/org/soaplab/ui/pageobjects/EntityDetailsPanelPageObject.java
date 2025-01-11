package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;
import static org.soaplab.ui.views.EntityDetailsPanel.PANEL_ID;

import org.soaplab.ui.fat.VaadinUtils;

public class EntityDetailsPanelPageObject {

	public PageObjectElement id() {
		return new PageObjectElement(byId(PANEL_ID + ".domain.entity.id"));
	}

	public PageObjectElement name() {
		return new PageObjectElement(byId(PANEL_ID + ".domain.entity.name"));
	}

	public PageObjectElement inci() {
		return new PageObjectElement(byId(PANEL_ID + ".domain.ingredient.inci"));
	}

	public void reset() {
		if (isInEditMode()) {
			name().pressEscape();
		}
	}

	public boolean isInEditMode() {
		return name().isEditable();
	}

	public void doubleClick() {
		VaadinUtils.doubleClickOnElement(byId(PANEL_ID));
	}

	public void click() {
		VaadinUtils.clickOnElement(byId(PANEL_ID));
	}
}
