package org.soaplab.ui.views;

import java.util.Optional;

import org.soaplab.domain.NamedEntity;

public interface EntityTableListener<T extends NamedEntity> {

	void selectionChanged(Optional<T> entity);

	void entityChanged(T entity);

	void enterEditMode();

	void leaveEditMode();
}