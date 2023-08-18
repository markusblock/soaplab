package org.soaplab.service.soapcalc;

import java.util.ArrayList;
import java.util.List;

import org.soaplab.service.soapcalc.SoapCalculatorService.CalculationIssue;

import lombok.Getter;

@Getter
public class SoapCalculatorIssueCollector {

	private final List<CalculationIssue> errors = new ArrayList<>();
	private final List<CalculationIssue> warnings = new ArrayList<>();

	public void addWarning(CalculationIssue issue) {
		warnings.add(issue);
	}

	public void addError(CalculationIssue issue) {
		errors.add(issue);
	}
	
	public boolean hasIssues() {
		return hasErrors() || hasWarnings();
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public boolean hasWarnings() {
		return !warnings.isEmpty();
	}
}
