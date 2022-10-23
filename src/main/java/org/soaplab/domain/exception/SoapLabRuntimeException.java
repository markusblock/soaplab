package org.soaplab.domain.exception;

public class SoapLabRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SoapLabRuntimeException() {
		super();
	}

	public SoapLabRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SoapLabRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SoapLabRuntimeException(String message) {
		super(message);
	}

	public SoapLabRuntimeException(Throwable cause) {
		super(cause);
	}

}
