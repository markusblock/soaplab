package org.soaplab.repository.microstream;

import org.springframework.stereotype.Component;

import one.microstream.integrations.spring.boot.types.config.StorageManagerInitializer;
import one.microstream.storage.types.StorageManager;

/**
 * This class initializes the {@link StorageManager} after it is created.
 */
@Component
public class CustomStorageManagerInitializer implements StorageManagerInitializer {


	@Override
	public void initialize(StorageManager storageManager) {
		// Add init stuff here
	}
}
