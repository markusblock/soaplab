package org.soaplab.api.rest;

import java.util.List;
import java.util.UUID;

import org.soaplab.domain.Acid;
import org.soaplab.repository.AcidRepository;
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
@RequestMapping("/acids")
public class AcidController {

	private AcidRepository repository;

	@Autowired
	public AcidController(AcidRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Acid> findAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	public Acid findById(@PathVariable("id") UUID id) {
		// TODO proper exception handling not found
		return repository.get(id);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody Acid resource) {
		// Preconditions.checkNotNull(resource);
		return repository.create(resource);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") UUID id) {
		repository.delete(id);
	}
}
