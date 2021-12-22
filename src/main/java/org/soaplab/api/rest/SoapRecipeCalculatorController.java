package org.soaplab.api.rest;

import org.soaplab.domain.CalculatedSoapReceiptResult;
import org.soaplab.domain.SoapReceipt;
import org.soaplab.service.SoapCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
public class SoapReceipeCalculatorController {

	private SoapCalculatorService soapCalculatorService;

	@Autowired
	public SoapReceipeCalculatorController(SoapCalculatorService soapCalculatorService) {
		this.soapCalculatorService = soapCalculatorService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CalculatedSoapReceiptResult calculcate(@RequestBody SoapReceipt soapReceipt) {
		return soapCalculatorService.calculate(soapReceipt);
	}

}
