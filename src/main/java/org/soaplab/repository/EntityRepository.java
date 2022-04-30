package org.soaplab.repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.soaplab.domain.NamedEntity;

public interface EntityRepository<T extends NamedEntity> extends Serializable {

	T create(T entity);

	void update(T entity);

	void delete(UUID id);

	T get(UUID id);

	List<T> findAll();

	List<T> findByName(String name);

	void storeAll();

	void deleteAll();

}
