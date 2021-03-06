package org.soaplab.repository.microstream;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import one.microstream.concurrency.XThreads;

@Component
@Slf4j
public abstract class EntityRepositoryMSImpl<T extends NamedEntity> implements EntityRepository<T> {

	protected final Map<UUID, T> idToEntity;
	protected final MicrostreamRepository repository;

	public EntityRepositoryMSImpl(MicrostreamRepository repository) {
		super();
		this.repository = repository;
		this.idToEntity = getIdToEntityMapping();
	}

	protected abstract Map<UUID, T> getIdToEntityMapping();

	@Override
	public T get(UUID id) {
		T entity = idToEntity.get(id);
		// TODO throwNotFoundExceptionIfRequired(fat);
		return (T) entity.getClone();
	}

	@Override
	public T create(T entity) {
		log.info("Adding new entity " + entity);

		// TODO throwDuplicateName(fat);

		UUID uuid = UUID.randomUUID();
		T entityCopy = (T) entity.toBuilder().id(uuid).build();

		XThreads.executeSynchronized(() -> {
			this.idToEntity.put(uuid, entityCopy);
			storeEntitiesInRepository();
		});
		return (T) entityCopy.toBuilder().build();
	}

	@Override
	public void update(T entity) {
		log.info("Updating entity " + entity);
		// would throw not found exception if not present
		get(entity.getId());
		T entityCopy = (T) entity.toBuilder().build();

		XThreads.executeSynchronized(() -> {
			this.idToEntity.put(entityCopy.getId(), entityCopy);
			storeEntitiesInRepository();
			storeCompositeEntitiesInRepository(entityCopy);
		});
	}

	@Override
	public void delete(UUID id) {
		T entity = get(id);
		log.info("Deleting entity " + entity);
		this.idToEntity.remove(id);
		storeEntitiesInRepository();
	}

	@Override
	public List<T> findAll() {
		return this.idToEntity.values().stream()
				.sorted(Comparator.comparing(NamedEntity::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@Override
	public List<T> findByName(String name) {
		return this.idToEntity.values().stream()
				.filter(entity -> entity.getName().toLowerCase().contains(name.toLowerCase()))
				.collect(Collectors.toList());
	}

	private void storeEntitiesInRepository() {
		repository.getStorage().store(this.idToEntity);
	}

	/**
	 * Implemented in subclasses to store referenced composite entities.
	 * 
	 * @param entity the parent entity that is stored and for this entity the
	 *               composite entities should be stored.
	 */
	protected void storeCompositeEntitiesInRepository(T entity) {
		// NoOp
	}

	private void deleteEntitiesInRepository() {
		log.info("Deleting all entities");
		this.idToEntity.clear();
		storeEntitiesInRepository();
	}
}
