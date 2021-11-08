package org.soaplab.api.rest;

import java.util.List;
import java.util.UUID;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fats")
public class FatController {

	private FatRepository repository;

	@Autowired
	public FatController(FatRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Fat> findAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	public Fat findById(@PathVariable("id") UUID id) {
		// TODO proper exception handling not found
		return repository.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody Fat resource) {
		// Preconditions.checkNotNull(resource);
		return repository.create(resource);
	}

//    @PutMapping(value = "/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void update(@PathVariable( "id" ) Long id, @RequestBody Fat resource) {
//        //Preconditions.checkNotNull(resource);
//        //RestPreconditions.checkNotNull(service.getById(resource.getId()));
//        repository.update(resource);
//    }

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") UUID id) {
		repository.delete(id);
	}
}
