package org.soaplab.repository.microstream;

import java.nio.file.Paths;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

@Component
@Slf4j
@Getter
public class MicrostreamRepository {

	private final EmbeddedStorageManager storage;
	private final DataRoot root;

	public MicrostreamRepository(@Value("${microstream.store.location}") final String location) {
		log.info("location " + location);
		root = new DataRoot();

		this.storage = EmbeddedStorage.Foundation(Paths.get(location)).onConnectionFoundation(cf -> cf
				.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader())))
				.start(root);
	}

	@PreDestroy
	public void destroy() {
		System.err.println("Shutting down storage");
		storage.shutdown();
	}
}
