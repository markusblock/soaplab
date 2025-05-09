package org.soaplab.repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.domain.exception.EntityNotFoundException;

public interface EntityRepository<T extends NamedEntity> extends Serializable {

	/**
	 * Creates an entity with the values provided.
	 *
	 * @param entity the entity with values that should be created.
	 * @return the created entity
	 * @throws DuplicateNameException in case an entity with the name already exists
	 */
	T create(T entity);

	/**
	 * Updates an entity with the values of the provided entity, except the id.
	 *
	 * @param entity the entity with values that should be updated. If an attribute
	 *               is <code>null</code> it will be set to <code>null</code>.
	 * @return the updated entity
	 * @throws DuplicateNameException in case an entity with the name already exists
	 */
	T update(T entity);

	/**
	 * Delete the entity specified by id. No exception will be thrown if the entity
	 * that should be deleted doesn't exist.
	 *
	 * @param id id of the entity
	 */
	void delete(UUID id);

	/**
	 * Retrieves an entity by id.
	 *
	 * @param id id of the entity
	 * @return the entity T
	 * @throws EntityNotFoundException if the entity couldn't be found
	 */
	T get(UUID id);

	List<T> findAll();

	List<T> findByName(String name);
}
