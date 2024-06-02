package org.soaplab.repository.microstream;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.store.afs.nio.types.NioFileSystem;
import org.eclipse.serializer.afs.types.ADirectory;
import org.eclipse.serializer.afs.types.AReadableFile;
import org.eclipse.serializer.collections.types.XSequence;
import org.eclipse.serializer.util.X;
import org.eclipse.store.storage.types.StorageConnection;
import org.eclipse.store.storage.types.StorageDataConverterCsvConfiguration;
import org.eclipse.store.storage.types.StorageDataConverterTypeBinaryToCsv;
import org.eclipse.store.storage.types.StorageEntityTypeConversionFileProvider;
import org.eclipse.store.storage.types.StorageEntityTypeExportFileProvider;
import org.eclipse.store.storage.types.StorageEntityTypeExportStatistics;
import org.eclipse.store.storage.types.StorageManager;
import org.eclipse.serializer.util.cql.CQL;

@Slf4j
public class MicrostreamDatabaseImportExport {

	private final StorageManager storageManager;
	private final String fileSuffix = "bin";

	public MicrostreamDatabaseImportExport(StorageManager storageManager) {
		this.storageManager = storageManager;
		// TODO Auto-generated constructor stub
	}

	public void export(String binFolder, boolean exportToJson) {
		final NioFileSystem fileSystem = NioFileSystem.New();
		log.error("=> exportDir: " + binFolder);
		final StorageConnection connection = storageManager.createConnection();
		final StorageEntityTypeExportStatistics exportResult = connection.exportTypes(
				new StorageEntityTypeExportFileProvider.Default(fileSystem.ensureDirectoryPath(binFolder), fileSuffix),
				typeHandler -> true // export all, customize if necessary
		);
		final XSequence<Path> exportFiles = CQL.from(exportResult.typeStatistics().values()).project(s -> {
			final Path path = Paths.get(s.file().identifier());
			log.error("Export " + path.toAbsolutePath());
			return path;
		}).execute();

		if (exportToJson) {
			final String csvExportDir = binFolder + "-csv";
			final StorageDataConverterTypeBinaryToCsv converter = new StorageDataConverterTypeBinaryToCsv.UTF8(
					StorageDataConverterCsvConfiguration.defaultConfiguration(),
					new StorageEntityTypeConversionFileProvider.Default(fileSystem.ensureDirectoryPath(csvExportDir),
							"csv"),
					storageManager.typeDictionary(), null, // no type name mapping
					4096, // read buffer size
					4096 // write buffer size
			);
			exportFiles.forEach(file -> {
				final AReadableFile dataFile = fileSystem.ensureFilePath(binFolder, file.toString()).useReading();
				try {
					log.error("Convert " + dataFile.toPathString());
					converter.convertDataFile(dataFile);
				} finally {
					dataFile.close();
				}
			});
		}
	}

	public void importDatabase(String binFolder) {
		try {
			final NioFileSystem fileSystem = NioFileSystem.New();
			final ADirectory path = fileSystem.ensureDirectoryPath(binFolder);
			if (path.exists() && !path.isEmpty()) {
				log.warn("importing data from " + binFolder);
				final StorageConnection connection = storageManager.createConnection();
				connection.importFiles(X.Enum(fileSystem.ensureDirectoryPath(binFolder).listFiles()));
			} else {
				log.warn("import folder is empty or doesn't exist " + binFolder);
			}
		} catch (final Exception e) {
			log.error("Error occured while importing data", e);
		}

	}
}
