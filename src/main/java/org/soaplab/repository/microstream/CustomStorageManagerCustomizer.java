package org.soaplab.repository.microstream;

import org.eclipse.serializer.reflect.ClassLoaderProvider;
import org.eclipse.store.integrations.spring.boot.types.converter.EclipseStoreConfigConverter;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageFoundationFactory;
import org.eclipse.store.storage.types.StorageManager;

/**
 * This class configures the {@link StorageManager} before it is created.
 *
 */
//@Component
public class CustomStorageManagerCustomizer extends EmbeddedStorageFoundationFactory {

	public CustomStorageManagerCustomizer(EclipseStoreConfigConverter converter,
			ClassLoaderProvider classLoaderProvider) {
		super(converter, classLoaderProvider);
		// TODO Auto-generated constructor stub
	}

	// see ApplicationConfiguration

//	@Override
//	public void customize(EmbeddedStorageFoundation foundation) {
//
//		foundation.onConnectionFoundation(cf -> {
//			cf.setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader()));
//			cf.setReferenceFieldEagerEvaluator(new StoreEagerEvaluator());
//			BinaryHandlersJDK17.registerJDK17TypeHandlers(cf);
//		});
//	}
}
