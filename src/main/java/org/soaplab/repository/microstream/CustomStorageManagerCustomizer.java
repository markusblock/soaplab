package org.soaplab.repository.microstream;

import org.springframework.stereotype.Component;

import one.microstream.integrations.spring.boot.types.config.EmbeddedStorageFoundationCustomizer;
import one.microstream.persistence.binary.jdk17.types.BinaryHandlersJDK17;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.embedded.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.StorageManager;

/**
 * This class configures the {@link StorageManager} before it is created.
 *
 */
@Component
public class CustomStorageManagerCustomizer implements EmbeddedStorageFoundationCustomizer {

	@Override
	public void customize(EmbeddedStorageFoundation<?> foundation) {

		foundation.onConnectionFoundation(cf -> cf
				.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())));
		foundation.onConnectionFoundation(BinaryHandlersJDK17::registerJDK17TypeHandlers);
	}
}
