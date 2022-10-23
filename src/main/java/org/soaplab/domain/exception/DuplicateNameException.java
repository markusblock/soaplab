package org.soaplab.domain.exception;

public class DuplicateNameException extends SoapLabRuntimeException {

	private static final String MESSAGE_DUPLICATE_NAME = "Entity with name '%s' already exists";
	private static final long serialVersionUID = 1L;

	public DuplicateNameException() {
		super();
	}

	public DuplicateNameException(String entityName, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(String.format(MESSAGE_DUPLICATE_NAME, entityName), cause, enableSuppression, writableStackTrace);
	}

	public DuplicateNameException(String entityName, Throwable cause) {
		super(String.format(MESSAGE_DUPLICATE_NAME, entityName), cause);
	}

	public DuplicateNameException(String entityName) {
		super(String.format(MESSAGE_DUPLICATE_NAME, entityName));
	}

	public DuplicateNameException(Throwable cause) {
		super(cause);
	}
}
