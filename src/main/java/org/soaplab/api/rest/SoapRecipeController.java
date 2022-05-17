package org.soaplab.api.rest;

import java.util.UUID;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.repository.SoapRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/soaprecipes")
public class SoapRecipeController {

	private SoapRecipeRepository repository;

	@Autowired
	public SoapRecipeController(SoapRecipeRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") UUID id) {
		SoapRecipe foundEntity = repository.get(id);
		if (foundEntity == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(foundEntity);
		}
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") UUID id) {
		repository.delete(id);
	}
}
