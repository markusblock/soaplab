package org.soaplab.ui.views;

import java.util.Optional;

import org.soaplab.domain.Entity;

public interface EntityTableListener<T extends Entity> {

	/**
	 * The selection in table has changed.
	 */
	void selectionChangedInEntityTable(Optional<T> entity);

	/**
	 * The entity selected in the table has changed at least one of its properties.
	 */
	void entityChangedInEntityTable(T entity);

	/**
	 * The entity table enters the edit mode.
	 */
	void entityTableEntersEditMode();

	/**
	 * The entity table leaves the edit mode.
	 */
	void entityTableLeavesEditMode();
}