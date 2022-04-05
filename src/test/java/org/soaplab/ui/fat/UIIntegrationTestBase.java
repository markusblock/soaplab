package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.open;

import java.io.File;
import java.time.Duration;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.ScreenShooterExtension;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith({ ScreenShooterExtension.class })
@Slf4j
public class UIIntegrationTestBase {

	@Autowired
	private Environment env;

	private static File databaseFolder;

	@LocalServerPort
	private Integer port;
	private TestInfo testInfo;

	@BeforeAll
	public static void baseBeforeAll() {
		Locale.setDefault(Locale.GERMAN);

		Configuration.browser = Browsers.FIREFOX;
		Configuration.reportsFolder = "target/surefire-reports";
		Configuration.timeout = Duration.ofSeconds(5).toMillis();
		Configuration.clickViaJs = true;
		Configuration.fastSetValue = true;

		MutableCapabilities browserOptions = null;
		if (WebDriverRunner.isFirefox() || WebDriverRunner.isLegacyFirefox()) {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("intl.accept_languages", Locale.getDefault().getLanguage());
			browserOptions = new FirefoxOptions().setHeadless(true).setProfile(profile);
		} else if (WebDriverRunner.isChrome()) {
			browserOptions = new ChromeOptions().setHeadless(true)
					.addArguments("--lang=" + Locale.getDefault().getLanguage());
		}
		Configuration.browserCapabilities = browserOptions;

		registerShutdownHook();
	}

	@BeforeEach
	public void baseBeforeEach(TestInfo testInfo) {
		this.testInfo = testInfo;
		Configuration.baseUrl = "http://localhost:" + port;
		open("/soaplab/ui/fats");

		VaadinUtils.waitUntilPageLoaded();

		String databaseFolderProperty = env.getProperty("microstream.store.location");
		if (databaseFolder == null) {
			databaseFolder = new File(databaseFolderProperty);
			log.info("Setting testdatabase to " + databaseFolder);
		}
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
			removeDatabaseFolder();
		};

		Thread shutdownThread = new Thread(shutdownTask, "Database Shutdown Thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
}