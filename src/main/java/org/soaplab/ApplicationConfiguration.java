package org.soaplab;

import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.integrations.spring.boot.types.configuration.EclipseStoreProperties;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageFoundationFactory;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageManagerFactory;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.soaplab.repository.microstream.DataRoot;
import org.soaplab.repository.microstream.StoreEagerEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationConfiguration {

	@Bean
	EmbeddedStorageManager configureStorage(@Autowired EclipseStoreProperties myConfiguration,
			@Autowired EmbeddedStorageManagerFactory managerFactory,
			@Autowired EmbeddedStorageFoundationFactory foundationFactory, @Autowired Environment environment) {
		// Modify the configuration
		// myConfiguration.setStorageDirectory(temp.getDir().getAbsolutePath());
		// Create a new StorageFoundation
		final EmbeddedStorageFoundation<?> storageFoundation = foundationFactory
				.createStorageFoundation(myConfiguration);

		storageFoundation.onConnectionFoundation(cf -> {
			cf.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader()));
			cf.setReferenceFieldEagerEvaluator(new StoreEagerEvaluator());
			// java17 handler already registered
			// BinaryHandlersJDK17.registerJDK17TypeHandlers(cf);
		});

		// Create a new StorageManager
		final EmbeddedStorageManager storageManager = managerFactory.createStorage(storageFoundation, false);
		storageManager.start();
		log.info("Initiliazing storage with directory "
				+ storageManager.configuration().fileProvider().baseDirectory().toPathString());
		return storageManager;
	}

	@Bean
	DataRoot getDataRoot(EmbeddedStorageManager storageManager) {
		DataRoot dataRoot;
		if (storageManager.root() == null) {
			// No existing Database found
			log.info("Creating empty database");
			dataRoot = new DataRoot();
			storageManager.setRoot(dataRoot);
			storageManager.storeRoot();
		} else {
			dataRoot = (DataRoot) storageManager.root();
			log.info("Opening existing database");
			// log.info("Existing database with %s".formatted(dataRoot));
		}

		return dataRoot;
	}

}
