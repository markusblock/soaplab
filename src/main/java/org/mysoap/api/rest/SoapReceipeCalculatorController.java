package org.mysoap.api.rest;

import org.mysoap.domain.CalculatedSoapReceiptResult;
import org.mysoap.domain.SoapReceipt;
import org.mysoap.service.SoapCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public CalculatedSoapReceiptResult calculcate(SoapReceipt soapReceipt) {
		return soapCalculatorService.calculate(soapReceipt);
	}

}
