package org.soaplab.repository.microstream;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.soaplab.domain.NamedEntity;
import org.soaplab.repository.EntityRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

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
		return entity;
	}

	@Override
	public UUID create(T entity) {
		entity.setId(UUID.randomUUID());
		log.info("Adding new entity " + entity);
		this.idToEntity.put(entity.getId(), entity);
		storeAll();
		return entity.getId();
	}

	@Override
	public void update(T entity) {
		log.info("Updating entity " + entity);
		get(entity.getId());
		repository.getStorage().store(entity);
	}

	@Override
	public void delete(UUID id) {
		T entity = get(id);
		log.info("Deleting entity " + entity);
		this.idToEntity.remove(id);
		storeAll();
	}

	@Override
	public List<T> findAll() {
		return List.copyOf(this.idToEntity.values());
	}

	@Override
	public List<T> findByName(String name) {
		return this.idToEntity.values().stream().filter(entity -> entity.getName().equals(name))
				.collect(Collectors.toList());
	}

	@Override
	public void create(T... entities) {
		for (T entity : entities) {
			create(entity);
		}
	}

	@Override
	public void storeAll() {
		repository.getStorage().store(this.idToEntity);
	}

	@Override
	public void deleteAll() {
		log.info("Deleting all entities");
		this.idToEntity.clear();
		storeAll();
	}
}
