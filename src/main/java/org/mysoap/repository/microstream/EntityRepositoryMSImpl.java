package org.mysoap.repository.microstream;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.mysoap.domain.NamedEntity;
import org.mysoap.repository.EntityRepository;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public abstract class EntityRepositoryMSImpl<T extends NamedEntity> implements EntityRepository<T> {

	protected final Map<Long, T> idToEntity;
	protected final MicrostreamRepository repository;

	public EntityRepositoryMSImpl(MicrostreamRepository repository) {
		super();
		this.repository = repository;
		this.idToEntity = getIdToEntityMapping();
	}

	protected abstract Map<Long, T> getIdToEntityMapping();

	@Override
	public T get(Long id) {
		T entity = idToEntity.get(id);
		// throwNotFoundExceptionIfRequired(fat);
		return entity;
	}

	@Override
	public Long add(T entity) {
		entity.setId(new Random().nextLong());
		log.info("Adding new entity " + entity);
		this.idToEntity.put(entity.getId(), entity);
		storeAll();
		return entity.getId();
	}

	@Override
	public void delete(Long id) {
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
	public void add(T... entities) {
		for (T entity : entities) {
			add(entity);
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
