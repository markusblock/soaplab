package org.soaplab;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.soaplab.TestSystemPropertyHelper.TestLocale;
import org.soaplab.ui.i18n.TranslationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.codeborne.selenide.WebDriverRunner;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class SoaplabApplicationIT {

	private static File databaseFolder;

	@LocalServerPort
	private Integer port;
	private TestInfo testInfo;

	@BeforeAll
	public static void baseBeforeAll(@Autowired Environment environment) {

		configureDefaultLocale();

		configureDatabaseFolder(environment);

		registerShutdownHook();

	}

	private static void configureDatabaseFolder(Environment environment) {
		String databaseFolderProperty = environment.getProperty("microstream.store.location");
		if (databaseFolder == null) {
			databaseFolder = new File(databaseFolderProperty);
			log.info("Setting database folder to " + databaseFolder);
		}
	}

	private static void configureDefaultLocale() {
		TestLocale locale = TestSystemPropertyHelper.getTestLocale();
		log.info("Setting locale to {}", locale);
		switch (locale) {
		case DE:
			Locale.setDefault(TranslationProvider.LOCALE_DE);
			break;
		case EN:
			Locale.setDefault(TranslationProvider.LOCALE_EN);
			break;
		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestLocale.class,
					Objects.toString(locale));
		}
	}

	@BeforeEach
	public void baseBeforeEach(TestInfo testInfo) {
		this.testInfo = testInfo;

	}

	protected String getTestName() {
		return testInfo.getTestMethod().get().getName();
	}

	private synchronized static void removeDatabaseFolder() {
		log.info("[....] Removing testdatabase: " + databaseFolder);
		try {
			if (databaseFolder != null) {
				String[] testDatabasesFolders = databaseFolder.getParentFile()
						.list((dir, name) -> name.startsWith("test-"));
				for (int i = 0; i < testDatabasesFolders.length; i++) {
					File fileToDelete = new File(databaseFolder.getParentFile(), testDatabasesFolders[i]);
					FileUtils.forceDelete(fileToDelete);
					log.info("[DONE] Removing testdatabase: " + fileToDelete);
				}
				databaseFolder = null;
			}
		} catch (Exception e) {
			log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolder, e);
		}
	}

	private static void registerShutdownHook() {
		Runnable shutdownTask = () -> {
			WebDriverRunner.closeWebDriver();
			removeDatabaseFolder();
		};

		Thread shutdownThread = new Thread(shutdownTask, "Database Shutdown Thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	@Test
	void contextLoads() {
		log.info("### test run success " + getTestName());
	}

}
