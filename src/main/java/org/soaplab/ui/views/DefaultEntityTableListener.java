package org.soaplab.ui.views;

import java.util.Optional;

import org.soaplab.domain.Entity;

public class DefaultEntityTableListener<ENTITY extends Entity> implements EntityTableListener<ENTITY> {

	@Override
	public void selectionChangedInEntityTable(Optional<ENTITY> entity) {
	}

	@Override
	public void entityChangedInEntityTable(ENTITY entity) {
	}

	@Override
	public void entityTableEntersEditMode() {
	}

	@Override
	public void entityTableLeavesEditMode() {
	}

}
