package org.soaplab.ui.views;

public interface EntityViewDetailsControllerCallback<T> {

	void editEntity(T entity);

	void cancelEditMode();

	void saveEntity(T entity);
}