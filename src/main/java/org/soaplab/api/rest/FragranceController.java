package org.soaplab.api.rest;

import java.util.List;
import java.util.UUID;

import org.soaplab.domain.Fragrance;
import org.soaplab.repository.FragranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fragrances")
public class FragranceController {

	private FragranceRepository repository;

	@Autowired
	public FragranceController(FragranceRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Fragrance> findAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	public Fragrance findById(@PathVariable("id") UUID id) {
		// TODO proper exception handling not found
		return repository.get(id);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody Fragrance resource) {
		// Preconditions.checkNotNull(resource);
		return repository.create(resource);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") UUID id) {
		repository.delete(id);
	}
}
