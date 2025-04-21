package org.soaplab.domain.exception;

import java.util.UUID;

public class DuplicateIdException extends SoapLabRuntimeException {

	private static final String MESSAGE_DUPLICATE_ID = "Entity with id '%s' already exists";
	private static final long serialVersionUID = 1L;

	public DuplicateIdException() {
		super();
	}

	public DuplicateIdException(UUID id, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(String.format(MESSAGE_DUPLICATE_ID, id), cause, enableSuppression, writableStackTrace);
	}

	public DuplicateIdException(UUID id, Throwable cause) {
		super(String.format(MESSAGE_DUPLICATE_ID, id), cause);
	}

	public DuplicateIdException(UUID id) {
		super(String.format(MESSAGE_DUPLICATE_ID, id));
	}

	public DuplicateIdException(Throwable cause) {
		super(cause);
	}
}
