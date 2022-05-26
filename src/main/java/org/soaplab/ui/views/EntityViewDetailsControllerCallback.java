package org.soaplab.ui.views;

public interface EntityViewDetailsControllerCallback<T> {

	void deleteEntity(T entity);

	void editEntity(T entity);

	void cancelEditMode();

	void createNewEntity();

	void saveEntity(T entity);
}