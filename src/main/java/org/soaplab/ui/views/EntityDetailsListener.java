package org.soaplab.ui.views;

public interface EntityDetailsListener<T> {

	/**
	 * The entity shown in the details panel has changed at least one of its
	 * properties.
	 */
	void entityChangedInEntityDetails(T entity);

	/**
	 * The entity details panel enters the edit mode.
	 */
	void entityDetailsPanelEntersEditMode();

	/**
	 * The entity details panel leaves the edit mode.
	 */
	void entityDetailsPanelLeavesEditMode();
}