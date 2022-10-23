package org.soaplab.repository.microstream;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.repository.EntityRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import one.microstream.concurrency.XThreads;

@Component
@Slf4j
public abstract class EntityRepositoryMSImpl<T extends NamedEntity> implements EntityRepository<T> {

	protected final MicrostreamRepository repository;

	public EntityRepositoryMSImpl(MicrostreamRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public T create(T entity) {
		log.info("Adding new entity " + entity);

		assertNoEntityWithNameExists(entity);

		final UUID uuid = UUID.randomUUID();
		final T entityCopy = (T) entity.toBuilder().id(uuid).build();

		XThreads.executeSynchronized(() -> {
			getEntitiesInternal().add(entityCopy);
			storeEntitiesInRepository();
			storeCompositeEntitiesInRepository(entityCopy);
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
		final T entity = get(id);
		log.info("Deleting entity " + entity);

		assertEntityIsNotReferencedByOtherEntities(entity);

		getEntitiesInternal().remove(entity);
		storeEntitiesInRepository();
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
	public T get(UUID id) {
		final T entity = getInternal(id);
		// TODO throwNotFoundExceptionIfRequired(fat);
		getAndReplaceCompositeEntitiesFromRepository(entity);
		return (T) entity.getCopyBuilder().build();
	}

	private T getInternal(UUID id) {
		return getEntitiesInternal().stream().filter(e -> e.getId().equals(id)).findFirst().orElseThrow();
	}

	protected void assertEntityIsNotReferencedByOtherEntities(T entity) {
		// noOp
	}

	protected void getAndReplaceCompositeEntitiesFromRepository(T entity) {
		// noOp
	}

	protected abstract Set<T> getEntitiesInternal();

	/**
	 * Implemented in subclasses to store referenced composite entities.
	 *
	 * @param entity the parent entity that is stored and for this entity the
	 *               composite entities should be stored.
	 */
	protected void storeCompositeEntitiesInRepository(T entity) {
		// NoOp
	}

	private void storeEntitiesInRepository() {
		repository.getStorage().store(this.getEntitiesInternal());
	}

	@Override
	public void update(T entity) {
		log.info("Updating entity " + entity);

		XThreads.executeSynchronized(() -> {
			// would throw not found exception if not present
			final T oldEntity = getInternal(entity.getId());

			if (!oldEntity.getName().equals(entity.getName())) {
				// if name has changed check that no entity with new name already exists
				assertNoEntityWithNameExists(entity);
			}

			// copy updated entity values to persistedEntity
			try {
				BeanUtils.copyProperties(oldEntity, entity);
				storeEntitiesInRepository();
				storeCompositeEntitiesInRepository(entity);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
