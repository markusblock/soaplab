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
import org.eclipse.serializer.concurrency.XThreads;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.SoaplabProperties;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.exception.DuplicateIdException;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.domain.exception.EntityNotFoundException;
import org.soaplab.repository.EntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public abstract class EntityRepositoryMSImpl<T extends NamedEntity> implements EntityRepository<T> {

	private static final long serialVersionUID = 1L;

	private final DataRoot dataRoot;
	private final SoaplabProperties properties;
	protected final EmbeddedStorageManager storageManager;

	@Override
	public T create(T entity) {
		log.debug("Adding new provided entity " + entity);
		Assert.notNull(entity, "Entity must not be null");
		Assert.notNull(entity.getId(), "Entity id must not be null");
		assertNoEntityWithIdExists(entity);
		assertNoEntityWithNameExists(entity);

		final T entityCopy = (T) entity.toBuilder().version(Long.valueOf(1)).build();
		log.info("Creating entity " + entityCopy);

		XThreads.executeSynchronized(() -> {
			storeEntityInRepository(entityCopy);
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

	private void assertNoEntityWithIdExists(T entity) {
		if (getInternalWithoutException(entity.getId()).isPresent()) {
			throw new DuplicateIdException(entity.getId());
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
		return getEntitiesInternal().stream().map(e -> get(e.getId()))
				.sorted(Comparator.comparing(NamedEntity::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@Override
	public List<T> findByName(String name) {
		return getEntitiesInternal().stream().map(e -> get(e.getId()))
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
		storageManager.store(this.getEntitiesInternal());
	}

	private void storeEntityInRepository(T entity) {
		storageManager.store(entity);
	}

	@Override
	public T update(T entity) {
		Assert.notNull(entity, "Entity must not be null");

		// would throw not found exception if not present
		final T persistedEntity = getInternal(entity.getId());

		XThreads.executeSynchronized(() -> {

			if (!persistedEntity.getName().equals(entity.getName())) {
				// if name has changed check that no entity with new name already exists
				assertNoEntityWithNameExists(entity);
			}

			// copy updated entity values to persistedEntity
			try {
				final long oldVersion = persistedEntity.getVersion();
				final long newVersion = oldVersion + 1;
				final NamedEntity entityWithAdaptedVertsion = entity.getCopyBuilder().version(newVersion).build();
				log.info("Updating entity {} with values {}", persistedEntity, entityWithAdaptedVertsion);
				BeanUtils.copyProperties(persistedEntity, entityWithAdaptedVertsion);
				storeEntityInRepository(persistedEntity);
				storeEntitiesInRepository();
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
		});

		// TODO EXPORT only temporary
		// new
		// MicrostreamDatabaseImportExport(storageManager).export(properties.getInitfolder(),
		// false);

		return (T) persistedEntity.toBuilder().build();
	}

	protected DataRoot getDataRoot() {
		// TODO remove log statements
//		log.error("repo root classloader: " + dataRoot.getClass().getClassLoader());
//		log.error("DataRoot classloader: " + DataRoot.class.getClassLoader());
		return dataRoot;
	}
}
