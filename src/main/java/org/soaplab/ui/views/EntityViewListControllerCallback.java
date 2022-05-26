package org.soaplab.ui.views;

import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;

public interface EntityViewListControllerCallback<T extends NamedEntity> {

	void entitySelected(T entity);

	EntityRepository<T> getRepository();

}