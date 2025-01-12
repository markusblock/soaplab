package org.soaplab.ui.pageobjects;

import org.openqa.selenium.By;

public class GridRowPageObject extends PageObjectElement {

	private final By gridLocator;

	public GridRowPageObject(By gridLocator, By rowLocator) {
		super(rowLocator);
		this.gridLocator = gridLocator;
	}
}
