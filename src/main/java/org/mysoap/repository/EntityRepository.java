package org.mysoap.repository;

import java.util.List;

import org.mysoap.domain.NamedEntity;

public interface EntityRepository<T extends NamedEntity> {

	Long add(T entity);

	void add(T... entities);

	void delete(Long id);

	T get(Long id);

	List<T> findAll();

	List<T> findByName(String name);

	void storeAll();

	void deleteAll();

}
