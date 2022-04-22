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
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.utility.DockerImageName;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.BrowserStrategyExtension;
import com.codeborne.selenide.junit5.ScreenShooterExtension;

import lombok.extern.slf4j.Slf4j;

@org.testcontainers.junit.jupiter.Testcontainers
@ExtendWith({ ScreenShooterExtension.class })
@ExtendWith({ BrowserStrategyExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class UIIntegrationTestBase {

	private static File databaseFolder;

	// will be shared between test methods
	public static BrowserWebDriverContainer<?> webDriverContainer;

	@LocalServerPort
	private Integer port;
	private TestInfo testInfo;

	@BeforeAll
	public static void baseBeforeAll(@Autowired Environment environment) {

		configureDefaultLocale();

		configureDatabaseFolder(environment);

		configureSelenideBaseSetup();

		BrowserConfiguration browserConfiguration = configureBrowser();

		setupTestEnvironment(environment, browserConfiguration.getMutableCapabilities(),
				browserConfiguration.getDockerImageName());

		registerShutdownHook();

		open("/soaplab/ui/fats");

		WebDriverRunner.getAndCheckWebDriver().manage().deleteAllCookies();
		Cookie cookie = new Cookie("locale", Locale.getDefault().toLanguageTag());
		WebDriverRunner.getAndCheckWebDriver().manage().addCookie(cookie);

		VaadinUtils.waitUntilPageLoaded();
	}

	private static BrowserConfiguration configureBrowser() {
		boolean isHeadless = TestSystemPropertyHelper.isHeadless();
		TestBrowser browser = TestSystemPropertyHelper.getBrowser();
		MutableCapabilities browserOptions = null;
		DockerImageName dockerImageName = null;
		switch (browser) {
		case CHROME:
			Configuration.browser = Browsers.CHROME;
			browserOptions = new ChromeOptions().setHeadless(isHeadless).addArguments("--no-sandbox")
					.addArguments("--disable-dev-shm-usage")
					.addArguments("--lang=" + Locale.getDefault().getLanguage());
			dockerImageName = DockerImageName.parse("selenium/standalone-chrome");
			break;

		case FIREFOX:
			Configuration.browser = Browsers.FIREFOX;
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("intl.accept_languages", Locale.getDefault().getLanguage());
			browserOptions = new FirefoxOptions().setProfile(profile).setHeadless(isHeadless)
					.addArguments("--no-sandbox").addArguments("--disable-dev-shm-usage");
			dockerImageName = DockerImageName.parse("selenium/standalone-firefox");
			break;

		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestBrowser.class,
					Objects.toString(browser));
		}
		log.info("Using browser {} with configuration {}", browser, browserOptions);

		return new BrowserConfiguration(browserOptions, dockerImageName);
	}

	private static void configureSelenideBaseSetup() {
		Configuration.reportsFolder = "target/failsafe-reports";
		Configuration.timeout = Duration.ofSeconds(5).toMillis();
		Configuration.clickViaJs = true;
		Configuration.fastSetValue = true;
	}

	private static void setupTestEnvironment(Environment environment, MutableCapabilities browserOptions,
			DockerImageName dockerImageName) {
		TestEnvironment testEnvironment = TestSystemPropertyHelper.getTestEnvironment();
		Integer port = environment.getProperty("local.server.port", Integer.class);
		switch (testEnvironment) {
		case LOCAL:
			Configuration.baseUrl = "http://localhost:" + port;
			Configuration.driverManagerEnabled = true;
			log.info("Setting up Selenide to use local browser and app at {}", Configuration.baseUrl);

			break;
		case DOCKER:
			Configuration.baseUrl = "http://host.testcontainers.internal:" + port;
			Configuration.driverManagerEnabled = false;
			log.info("Setting up Selenide to use app at {}", Configuration.baseUrl);
			webDriverContainer = new BrowserWebDriverContainer(dockerImageName).withCapabilities(browserOptions);

			log.info("Setting up testcontainers with browser in docker  {}", dockerImageName);

			log.info("Starting container ...");
			webDriverContainer.start();
			log.info(webDriverContainer.getContainerInfo().toString());
			log.info("Container is started");

			log.info("Exposing port {}", port);
			// exposing the host port to the container so the browser inside the container
			// can access it
			Testcontainers.exposeHostPorts(port);

			WebDriverRunner.setWebDriver(webDriverContainer.getWebDriver());
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

	private static class BrowserConfiguration {

		private MutableCapabilities browserOptions;
		private DockerImageName dockerImageName;

		BrowserConfiguration(MutableCapabilities browserOptions, DockerImageName dockerImageName) {
			this.browserOptions = browserOptions;
			this.dockerImageName = dockerImageName;
		}

		MutableCapabilities getMutableCapabilities() {
			return browserOptions;
		}

		DockerImageName getDockerImageName() {
			return dockerImageName;
		}
	}
}
