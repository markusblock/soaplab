package org.soaplab.ui.pageobjects;

import org.soaplab.domain.Fat;

public class FatTablePanelPageObject extends EntityTablePanelPageObject {

	public static String COLUMN_HEADER_INCI = "Inci";

	public FatTablePanelPageObject() {
		super("entitygrid.%s".formatted(Fat.class.getSimpleName()));
	}

}
