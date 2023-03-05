package org.soaplab.repository.microstream;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.domain.exception.EntityNotFoundException;
import org.soaplab.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import one.microstream.concurrency.XThreads;
import one.microstream.storage.types.StorageManager;

@Component
@Slf4j
public abstract class EntityRepositoryMSImpl<T extends NamedEntity> implements EntityRepository<T> {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DataRoot dataRoot;

	@Autowired
	protected StorageManager repository;

	@Override
	public T create(T entity) {
		Assert.notNull(entity, "Entity must not be null");
		assertNoEntityWithNameExists(entity);

		log.info("Adding new entity " + entity);

		final UUID uuid = UUID.randomUUID();
		final T entityCopy = (T) entity.toBuilder().id(uuid).build();

		XThreads.executeSynchronized(() -> {
			getEntitiesInternal().add(entityCopy);
			storeEntitiesInRepository();
		});
		return (T) entityCopy.toBuilder().build();
	}

	private void assertNoEntityWithNameExists(T entity) {
		final List<T> findByName = findByName(entity.getName());
		if (!CollectionUtils.isEmpty(findByName)) {
			for (final T foundEntityByName : findByName) {
				if (!foundEntityByName.getId().equals(entity.getId())) {
					throw new DuplicateNameException(entity.getName());
				}
			}
		}
	}

	@Override
	public void delete(UUID id) {
		Assert.notNull(id, "Id must not be null");

		getInternalWithoutException(id).ifPresentOrElse(entity -> {
			log.info("Deleting entity " + entity);

			assertEntityIsNotReferencedByOtherEntities(entity);

			getEntitiesInternal().remove(entity);
			storeEntitiesInRepository();
		}, () -> log.info("Entity couldn't be deleted because entity with id '" + id + "' doesn't exist"));
	}

	private void deleteEntitiesInRepository() {
		log.info("Deleting all entities");
		getEntitiesInternal().clear();
		storeEntitiesInRepository();
	}

	@Override
	public List<T> findAll() {
		return getEntitiesInternal().stream()
				.sorted(Comparator.comparing(NamedEntity::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@Override
	public List<T> findByName(String name) {
		return getEntitiesInternal().stream()
				.filter(entity -> entity.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public T get(UUID id) throws EntityNotFoundException {
		Assert.notNull(id, "Id must not be null");

		log.debug("Get entity for id " + id);

		final T entity = getInternal(id);
		getAndReplaceCompositeEntitiesFromRepository(entity);
		return (T) entity.getCopyBuilder().build();
	}

	private T getInternal(UUID id) {
		return getEntitiesInternal().stream().filter(e -> e.getId().equals(id)).findFirst()
				.orElseThrow(() -> new EntityNotFoundException(id));
	}

	private Optional<T> getInternalWithoutException(UUID id) {
		return getEntitiesInternal().stream().filter(e -> e.getId().equals(id)).findFirst();
	}

	protected void assertEntityIsNotReferencedByOtherEntities(T entity) {
		// noOp
	}

	protected void getAndReplaceCompositeEntitiesFromRepository(T entity) {
		// noOp
	}

	protected abstract Set<T> getEntitiesInternal();

	private void storeEntitiesInRepository() {
		repository.store(this.getEntitiesInternal());
	}

	private void storeEntityInRepository(T entity) {
		repository.store(entity);
	}

	@Override
	public T update(T entity) {
		Assert.notNull(entity, "Entity must not be null");

		log.info("Updating entity " + entity);

		// would throw not found exception if not present
		final T persistedEntity = getInternal(entity.getId());

		XThreads.executeSynchronized(() -> {

			if (!persistedEntity.getName().equals(entity.getName())) {
				// if name has changed check that no entity with new name already exists
				assertNoEntityWithNameExists(entity);
			}

			// copy updated entity values to persistedEntity
			try {
				BeanUtils.copyProperties(persistedEntity, entity);
				storeEntityInRepository(persistedEntity);
				storeEntitiesInRepository();
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
		});
		return (T) persistedEntity.toBuilder().build();
	}

	protected DataRoot getDataRoot() {
		// TODO remove log statements
//		log.error("repo root classloader: " + dataRoot.getClass().getClassLoader());
//		log.error("DataRoot classloader: " + DataRoot.class.getClassLoader());
		return dataRoot;
	}
}
