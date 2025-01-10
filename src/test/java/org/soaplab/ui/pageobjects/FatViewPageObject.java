package org.soaplab.ui.pageobjects;

public class FatViewPageObject extends EntityViewPageObject<FatTablePanelPageObject, FatDetailsPageObject> {

	private final FatTablePanelPageObject entityTablePanelPageObject;
	private final FatDetailsPageObject fatDetails;

	public FatViewPageObject() {
		entityTablePanelPageObject = new FatTablePanelPageObject();
		fatDetails = new FatDetailsPageObject();
	}

	@Override
	public FatDetailsPageObject getEntityDetails() {
		return fatDetails;
	}

	@Override
	public FatTablePanelPageObject getEntityTable() {
		return entityTablePanelPageObject;
	}
}
