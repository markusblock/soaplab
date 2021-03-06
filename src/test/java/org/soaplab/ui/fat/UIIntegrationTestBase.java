package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.open;

import java.io.File;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.soaplab.TestSystemPropertyHelper;
import org.soaplab.TestSystemPropertyHelper.TestBrowser;
import org.soaplab.TestSystemPropertyHelper.TestEnvironment;
import org.soaplab.TestSystemPropertyHelper.TestLocale;
import org.soaplab.ui.i18n.TranslationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.BrowserStrategyExtension;
import com.codeborne.selenide.junit5.ScreenShooterExtension;

import lombok.extern.slf4j.Slf4j;

@ExtendWith({ ScreenShooterExtension.class })
@ExtendWith({ BrowserStrategyExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class UIIntegrationTestBase {

	private static File databaseFolder;

	@LocalServerPort
	private Integer port;
	private TestInfo testInfo;

	@BeforeAll
	public static void baseBeforeAll(@Autowired Environment environment) {

		configureDefaultLocale();

		configureDatabaseFolder(environment);

		configureSelenideBaseSetup();

		configureBrowser();

		setupTestEnvironment(environment);

		registerShutdownHook();

		open("/soaplab/ui/fats");

		WebDriverRunner.getAndCheckWebDriver().manage().deleteAllCookies();
		Cookie cookie = new Cookie("locale", Locale.getDefault().toLanguageTag());
		WebDriverRunner.getAndCheckWebDriver().manage().addCookie(cookie);

		VaadinUtils.waitUntilPageLoaded();
	}

	private static void configureBrowser() {
		boolean isHeadless = TestSystemPropertyHelper.isHeadless();
		TestBrowser browser = TestSystemPropertyHelper.getBrowser();
		MutableCapabilities browserOptions = null;
		switch (browser) {
		case CHROME:
			Configuration.browser = Browsers.CHROME;
			browserOptions = new ChromeOptions().setHeadless(isHeadless).addArguments("--no-sandbox")
					.addArguments("--disable-dev-shm-usage")
					.addArguments("--lang=" + Locale.getDefault().getLanguage());
			break;

		case FIREFOX:
			Configuration.browser = Browsers.FIREFOX;
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("intl.accept_languages", Locale.getDefault().getLanguage());
			browserOptions = new FirefoxOptions().setProfile(profile).setHeadless(isHeadless)
					.addArguments("--no-sandbox").addArguments("--disable-dev-shm-usage");
			break;

		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestBrowser.class,
					Objects.toString(browser));
		}
		Configuration.headless = isHeadless;
		Configuration.browserCapabilities = browserOptions;
		log.info("Using browser {} with configuration {}", browser, browserOptions);
	}

	private static void configureSelenideBaseSetup() {
		Configuration.reportsFolder = "target/failsafe-reports";
		Configuration.timeout = Duration.ofSeconds(5).toMillis();
		Configuration.clickViaJs = true;
		Configuration.fastSetValue = true;
	}

	private static void setupTestEnvironment(Environment environment) {
		TestEnvironment testEnvironment = TestSystemPropertyHelper.getTestEnvironment();
		Integer port = environment.getProperty("local.server.port", Integer.class);
		switch (testEnvironment) {
		case LOCAL:
			Configuration.baseUrl = "http://localhost:" + port;
			Configuration.driverManagerEnabled = true;
			log.info("Setting up Selenide to use local browser and app at {}", Configuration.baseUrl);
			break;
		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestEnvironment.class,
					Objects.toString(testEnvironment));
		}
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
				// also remove old testdatabase files&folders
				String[] testDatabasesFolders = databaseFolder.getParentFile()
						.list((dir, name) -> name.startsWith("test-"));
				for (int i = 0; i < testDatabasesFolders.length; i++) {
					try {
						File fileToDelete = new File(databaseFolder.getParentFile(), testDatabasesFolders[i]);
						if (!fileToDelete.exists()) {
							continue;
						}
						FileUtils.forceDelete(fileToDelete);
						log.info("[DONE] Removing testdatabase: " + fileToDelete);
					} catch (Exception e) {
						log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolder, e);
					}
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
}
