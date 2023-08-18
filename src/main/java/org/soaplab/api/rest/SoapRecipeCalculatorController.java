package org.soaplab.api.rest;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.service.soapcalc.SoapCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("soaplab/rest/calculator")
public class SoapRecipeCalculatorController {

	private SoapCalculatorService soapCalculatorService;

	@Autowired
	public SoapRecipeCalculatorController(SoapCalculatorService soapCalculatorService) {
		this.soapCalculatorService = soapCalculatorService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> calculcate(@RequestBody SoapRecipe soapReceipt) {
		return ResponseEntity.ok(soapCalculatorService.calculate(soapReceipt));
	}

}
