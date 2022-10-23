package org.soaplab.domain.exception;

import org.soaplab.domain.Entity;

import lombok.Getter;

@Getter
public class EntityDeletionFailedException extends SoapLabRuntimeException {

	private static final String MESSAGE = "Entity deletion failed for entity %s. Reason: %s";
	private static final long serialVersionUID = 1L;
	private Entity entity;
	private REASON reason;

	public enum REASON {
		ENTITY_STILL_REFERENCED
	}

	public EntityDeletionFailedException() {
		super();
	}

	public EntityDeletionFailedException(Entity entity, REASON reason) {
		super(String.format(MESSAGE, entity, reason));
		this.entity = entity;
		this.reason = reason;
	}

}
