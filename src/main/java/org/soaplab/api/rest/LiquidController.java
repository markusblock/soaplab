package org.soaplab.api.rest;

import java.util.UUID;

import org.soaplab.domain.Liquid;
import org.soaplab.repository.LiquidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/liquids")
public class LiquidController {

	private LiquidRepository repository;

	@Autowired
	public LiquidController(LiquidRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") UUID id) {
		Liquid foundEntity = repository.get(id);
		if (foundEntity == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(foundEntity);
		}
	}

	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Liquid create(@RequestBody Liquid resource) {
		// Preconditions.checkNotNull(resource);
		// TODO switch to ResponseEntity
		return repository.create(resource);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") UUID id) {
		// TODO switch to ResponseEntity
		repository.delete(id);
	}
}
