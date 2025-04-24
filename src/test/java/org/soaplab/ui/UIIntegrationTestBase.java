package org.soaplab.ui;

import static com.codeborne.selenide.Selenide.open;

import java.io.File;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.soaplab.TestSystemPropertyHelper;
import org.soaplab.TestSystemPropertyHelper.TestEnvironment;
import org.soaplab.TestSystemPropertyHelper.TestLocale;
import org.soaplab.ui.i18n.TranslationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.utility.DockerImageName;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
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

	private static String databaseFolderPath;

	private static MutableCapabilities browserOptions;

	// static instance used by all tests. Without static started/stopped after each
	// test
	public static BrowserWebDriverContainer<?> webDriverContainer = new BrowserWebDriverContainer<>(
			isARM64() ? DockerImageName.parse(getSeleniarmImageName()).asCompatibleSubstituteFor(getSeleniumImageName())
					: DockerImageName.parse(getSeleniumImageName()))
			.withCapabilities(getBrowserOptions());

	@LocalServerPort
	private Integer port;
	private TestInfo testInfo;

	private static MutableCapabilities getBrowserOptions() {
		final boolean isHeadless = TestSystemPropertyHelper.isHeadless();
		Configuration.headless = isHeadless;
		switch (TestSystemPropertyHelper.getBrowser()) {
		case CHROME:
			Configuration.browser = Browsers.CHROME;
			final ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--no-sandbox").addArguments("--disable-dev-shm-usage")
					.addArguments("--lang=" + Locale.getDefault().getLanguage())
					.addArguments("--remote-allow-origins=*");
			if (isHeadless) {
				chromeOptions.addArguments("--headless");
			}
			browserOptions = chromeOptions;
			break;

		case FIREFOX:
			Configuration.browser = Browsers.FIREFOX;
			final FirefoxProfile firefoxProfile = new FirefoxProfile();
			final FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxProfile.setPreference("intl.accept_languages", Locale.getDefault().getLanguage());
			firefoxOptions.setProfile(firefoxProfile).addArguments("--no-sandbox")
					.addArguments("--disable-dev-shm-usage");
			if (isHeadless) {
				firefoxOptions.addArguments("--headless");
			}
			browserOptions = firefoxOptions;
			break;

		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestBrowser.class,
					Objects.toString(TestSystemPropertyHelper.getBrowser()));
		}

		return browserOptions;
	}

	private static String getSeleniarmImageName() {
		switch (TestSystemPropertyHelper.getBrowser()) {
		case CHROME:
			return "seleniarm/standalone-chromium";

		case FIREFOX:
			return "seleniarm/standalone-firefox";

		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestBrowser.class,
					Objects.toString(TestSystemPropertyHelper.getBrowser()));
		}
	}

	private static String getSeleniumImageName() {
		switch (TestSystemPropertyHelper.getBrowser()) {
		case CHROME:
			return "selenium/standalone-chrome";

		case FIREFOX:
			return "selenium/standalone-firefox";

		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestBrowser.class,
					Objects.toString(TestSystemPropertyHelper.getBrowser()));
		}
	}

	private static boolean isARM64() {
		return System.getProperty("os.arch").equals("aarch64");
	}

	@BeforeAll
	public static void baseBeforeAll(@Autowired Environment environment,
			@Autowired EmbeddedStorageManager storageManager) {

		// supress Vaadin devmode popups that prevent clicking on buttons beneath
		System.setProperty("vaadin.devmode.devTools.enabled", "false");

		configureDefaultLocale();

		configureDatabaseFolder(storageManager);

		configureSelenideBaseSetup();

		setupTestEnvironment(environment);

		registerShutdownHook();

		final String url = Configuration.baseUrl + "/soaplab/ui/fats";
		log.info("Openeing browser with URL " + url);
		open(url);

		// TODO find replacement for cookies (DB or session?)
		WebDriverRunner.getAndCheckWebDriver().manage().deleteAllCookies();
		final Cookie cookie = new Cookie("locale", Locale.getDefault().toLanguageTag());
		WebDriverRunner.getAndCheckWebDriver().manage().addCookie(cookie);

		VaadinUtils.waitUntilPageLoaded();
	}

	private static void configureSelenideBaseSetup() {
		Configuration.reportsFolder = "target/failsafe-reports";
		Configuration.timeout = Duration.ofSeconds(5).toMillis();
		Configuration.clickViaJs = true;
		Configuration.fastSetValue = true;
	}

	private static void setupTestEnvironment(Environment environment) {
		final TestEnvironment testEnvironment = TestSystemPropertyHelper.getTestEnvironment();
		final Integer port = environment.getProperty("local.server.port", Integer.class);
		log.info("Using browser options " + browserOptions);
		switch (testEnvironment) {
		case LOCAL:
			Configuration.baseUrl = "http://localhost:" + port;
			log.info("Setting up Selenide to use local browser and app at {}", Configuration.baseUrl);
			break;
		case CONTAINER:

			log.info("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE={}",
					System.getenv("TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"));
			log.info("DOCKER_HOST={}", System.getenv("DOCKER_HOST"));
			log.info("TESTCONTAINERS_RYUK_DISABLED={}", System.getenv("TESTCONTAINERS_RYUK_DISABLED"));

			Configuration.baseUrl = String.format("http://host.testcontainers.internal:%d", port);
			log.info("Setting up Selenide to use browser in container at {}", Configuration.baseUrl);

			Testcontainers.exposeHostPorts(port);
			webDriverContainer.start();
			log.info("Open VNC conncection with: open " + webDriverContainer.getVncAddress());

			log.info("Starting webdriver with selenium address " + webDriverContainer.getSeleniumAddress());
			Configuration.remote = webDriverContainer.getSeleniumAddress().toString();
			final RemoteWebDriver remoteWebDriver = new RemoteWebDriver(webDriverContainer.getSeleniumAddress(),
					browserOptions);
			WebDriverRunner.setWebDriver(remoteWebDriver);

			break;
		default:
			throw new EnumConstantNotPresentException(TestSystemPropertyHelper.TestEnvironment.class,
					Objects.toString(testEnvironment));
		}
	}

	private static void configureDatabaseFolder(EmbeddedStorageManager storageManager) {
		databaseFolderPath = storageManager.configuration().fileProvider().baseDirectory().toPathString();
		log.info("Using database folder for tests" + databaseFolderPath);
	}

	private static void configureDefaultLocale() {
		final TestLocale locale = TestSystemPropertyHelper.getTestLocale();
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

	public void selectMenuItem(String menuItemName) {
		final SelenideElement menuItem = Selenide.$$("vaadin-tabs vaadin-tab a span")
				.filterBy(Condition.text(menuItemName)).first();
		menuItem.click();
		VaadinUtils.waitUntilPageLoaded();
	}

	private synchronized static void removeDatabaseFolder() {
		log.info("[....] Removing testdatabase: " + databaseFolderPath);

		try {
			if (databaseFolderPath != null) {
				final File databaseFolder = new File(databaseFolderPath);
				if (databaseFolder.exists()) {
					// also remove old testdatabase files&folders
					final String[] testDatabasesFolders = databaseFolder.getParentFile()
							.list((dir, name) -> name.startsWith("test-"));
					if (testDatabasesFolders != null) {
						for (int i = 0; i < testDatabasesFolders.length; i++) {
							try {
								final File fileToDelete = new File(databaseFolder.getParentFile(),
										testDatabasesFolders[i]);
								if (!fileToDelete.exists()) {
									continue;
								}
								FileUtils.forceDelete(fileToDelete);
								log.info("[DONE] Removing testdatabase: " + fileToDelete);
							} catch (final Exception e) {
								log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolderPath,
										e);
							}
						}
					}
				}

				databaseFolderPath = null;
			}
		} catch (final Exception e) {
			log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolderPath, e);
		}
	}

	private static void registerShutdownHook() {
		final Runnable shutdownTask = () -> {
			WebDriverRunner.closeWebDriver();
			removeDatabaseFolder();
		};

		final Thread shutdownThread = new Thread(shutdownTask, "Database Shutdown Thread");
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
}
