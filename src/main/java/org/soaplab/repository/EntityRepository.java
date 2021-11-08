package org.soaplab.repository;

import java.util.List;
import java.util.UUID;

import org.soaplab.domain.NamedEntity;

public interface EntityRepository<T extends NamedEntity> {

	UUID create(T entity);

	void create(T... entities);

	void update(T entity);

	void delete(UUID id);

	T get(UUID id);

	List<T> findAll();

	List<T> findByName(String name);

	void storeAll();

	void deleteAll();

}
