package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Selectors.byId;

import org.soaplab.ui.fat.VaadinUtils;

import com.codeborne.selenide.Selenide;

public abstract class EntityViewPageObject<TABLE extends EntityTablePanelPageObject, DETAILS extends EntityDetailsPanelPageObject> {

	public EntityViewPageObject() {
	}

	public abstract DETAILS getEntityDetails();

	public abstract TABLE getEntityTable();

	public void refreshPage() {
		Selenide.refresh();
		VaadinUtils.waitUntilPageLoaded();
	}

	public void reset() {
		getEntityDetails().reset();
		getEntityTable().reset();
	}

	public PageObjectElement buttonAdd() {
		return new PageObjectElement(byId("entity.add"));
	}

	public PageObjectElement buttonRemove() {
		return new PageObjectElement(byId("entity.remove"));
	}

}
