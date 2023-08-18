package org.soaplab.service.soapcalc;

import java.util.List;

import org.soaplab.domain.SoapRecipe;
import org.soaplab.domain.exception.SoapLabRuntimeException;
import org.soaplab.service.soapcalc.SoapCalculatorService.CalculationIssue;
import org.springframework.core.env.Environment;

public class SoapCalculatorException extends SoapLabRuntimeException {

	private static final String ERROR_SOAPRECIPE_CALCULATION = "error.soaprecipe.calculation.";

	private static final long serialVersionUID = 1L;


	private Environment env;
	private SoapRecipe soapRecipe;

	private SoapCalculatorIssueCollector issueCollector;

	public SoapCalculatorException(Environment env, SoapRecipe soapRecipe,
			SoapCalculatorIssueCollector issueCollector) {
		super(String.format(env.getProperty(ERROR_SOAPRECIPE_CALCULATION + "intro"), soapRecipe.getId()));
		this.env = env;
		this.soapRecipe = soapRecipe;
		this.issueCollector = issueCollector;
	}

	public List<CalculationIssue> getErrors() {
		return issueCollector.getErrors();
	}

	public List<CalculationIssue> getWarnings() {
		return issueCollector.getWarnings();
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder(super.getMessage());
		issueCollector.getErrors().forEach(issue -> builder.append(getMessageForIssue(issue)));
		issueCollector.getWarnings().forEach(issue -> builder.append(getMessageForIssue(issue)));
		return builder.toString();
	}

	private String getMessageForIssue(CalculationIssue issue) {
		String message;
		switch (issue) {
		case NO_FAT_IN_RECIPE:
			message = env.getProperty(ERROR_SOAPRECIPE_CALCULATION + "nofatinrecipe");
			break;

		default:
			throw new IllegalArgumentException(String.format("'%s' is not handled.", issue));
		}
		return message;
	}

}
