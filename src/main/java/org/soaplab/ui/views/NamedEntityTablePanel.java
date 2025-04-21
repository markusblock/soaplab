package org.soaplab.ui.views;

import org.soaplab.domain.NamedEntity;

public class NamedEntityTablePanel<T extends NamedEntity> extends EntityTablePanel<T> {

	private static final long serialVersionUID = 1L;

	public NamedEntityTablePanel(Class<T> entityClass, EntityTableListener<T> listener) {
		super(entityClass, listener);

		addEntityColumn(NamedEntity.Fields.name, "domain.entity.name");
	}
}
