package org.soaplab.api.rest;

import java.util.List;
import java.util.UUID;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.exception.DuplicateNameException;
import org.soaplab.domain.exception.EntityNotFoundException;
import org.soaplab.repository.EntityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EntityController<T extends NamedEntity> {

	private final EntityRepository<T> repository;

	public EntityController(EntityRepository<T> repository) {
		this.repository = repository;
	}

	@GetMapping()
	public ResponseEntity<List<T>> findAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<T> findById(@PathVariable("id") UUID id) {
		final T foundEntity = repository.get(id);
		if (foundEntity == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(foundEntity);
		}
	}

	@PostMapping
	public ResponseEntity<T> create(@RequestBody T resource) {
		// null check is done within Spring
		final T createdEntity = repository.create(resource);
		return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<T> update(@PathVariable("id") UUID id, @RequestBody T entity) {
		// Preconditions.checkNotNull(resource);
		if (entity == null) {
			System.out.println();
		}
		final T updatedEntity = repository.update(entity);
		return ResponseEntity.ok(updatedEntity);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
		repository.delete(id);
		return ResponseEntity.noContent().build();
	}

	// Convert a predefined exception to an HTTP Status code
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Entity with name already exists") // 400
	@ExceptionHandler(DuplicateNameException.class)
	public void duplicateName() {
		// NoOp
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found") // 404
	@ExceptionHandler(EntityNotFoundException.class)
	public void entityNotFound() {
		// NoOp
	}
}
