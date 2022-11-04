package org.soaplab.domain.exception;

import java.util.UUID;

public class EntityNotFoundException extends SoapLabRuntimeException {

	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "Entity with id '%s' not found.";

	public EntityNotFoundException(UUID id) {
		super(String.format(MESSAGE, id));
	}

}
