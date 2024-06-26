package org.soaplab.repository.microstream;

import org.eclipse.store.storage.types.StorageManager;
import org.springframework.stereotype.Component;

/**
 * This class initializes the {@link StorageManager} after it is created.
 */
@Component
public class CustomStorageManagerInitializer // implements StorageManagerInitializer
{

//	@Autowired
//	private SoaplabProperties properties;
//
//	@Override
//	public void initialize(StorageManager storageManager) {
//		// Add init stuff here
//
//		// import database if empty
//		if (storageManager.root() == null || isDatabaseEmpty(storageManager)) {
//			// database empty
//			// TODO error while importing database
//			new MicrostreamDatabaseImportExport(storageManager).importDatabase(properties.getInitfolder());
//		}
//	}
//
//	private boolean isDatabaseEmpty(StorageManager storageManager) {
//		final Object root = storageManager.root();
//		if (root instanceof DataRoot) {
//			final DataRoot dataRoot = (DataRoot) root;
//			return CollectionUtils.isEmpty(dataRoot.getAllAcids()) || CollectionUtils.isEmpty(dataRoot.getAllFats())
//					|| CollectionUtils.isEmpty(dataRoot.getAllFragrances())
//					|| CollectionUtils.isEmpty(dataRoot.getAllKOH())
//					|| CollectionUtils.isEmpty(dataRoot.getAllLiquids())
//					|| CollectionUtils.isEmpty(dataRoot.getAllNaOH())
//					|| CollectionUtils.isEmpty(dataRoot.getAllSoapRecipes());
//		}
//		// if not instanceof DataRoot than root was loaded by another Classloader means
//		// first run
//		return true;
//	}
}
