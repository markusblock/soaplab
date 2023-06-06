package org.soaplab.api.rest;

import org.soaplab.domain.utils.OliveOilSoapRecipeRepositoryTestData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("soaplab/rest/testdata")
@Slf4j
@RequiredArgsConstructor
public class TestDataController {

	private final OliveOilSoapRecipeRepositoryTestData testData;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create() {
		log.info("creating test data");
		testData.createSoapRecipe();
	}

	@DeleteMapping
	public void delete() {
		log.info("deleting test data");
		testData.deleteSoapRecipe();
	}
}
