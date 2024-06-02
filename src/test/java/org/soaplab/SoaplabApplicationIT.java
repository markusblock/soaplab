package org.soaplab;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.skyscreamer.jsonassert.JSONAssert;
import org.soaplab.TestSystemPropertyHelper.TestLocale;
import org.soaplab.domain.Fat;
import org.soaplab.ui.fat.RepositoryTestHelper;
import org.soaplab.ui.fat.VaadinUtils;
import org.soaplab.ui.i18n.TranslationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.BrowserStrategyExtension;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith({ ScreenShooterExtension.class })
@ExtendWith({ BrowserStrategyExtension.class })
//@SpringJUnitWebConfig()
@ActiveProfiles("test")
@Slf4j
@Disabled
class SoaplabApplicationIT {

	private static File databaseFolder;

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RepositoryTestHelper repoHelper;

	private TestInfo testInfo;

	@BeforeAll
	public static void baseBeforeAll(@Autowired Environment environment) {

		configureDefaultLocale();

		configureDatabaseFolder(environment);

		configureSelenideBaseSetup();

		registerShutdownHook();
		final Integer port = environment.getProperty("local.server.port", Integer.class);
		Configuration.baseUrl = "http://127.0.0.1:" + port;
		log.info("Setting up Selenide to use app at {}", Configuration.baseUrl);

	}

	@BeforeEach
	public void baseBeforeEach(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	@Test
	void contextLoads(ApplicationContext context) throws Exception {

		assertThat(context).isNotNull();

		final Fat fat = repoHelper.createFat();

		final ResponseEntity<String> response = restTemplate
				.getForEntity(createURLWithPort("/soaplab/rest/fats/" + fat.getId()), String.class);
		System.out.println("RESPONSE\n" + response.getBody());

		final ResponseEntity<String> response2 = restTemplate.getForEntity(createURLWithPort("/soaplab/ui/fats/"),
				String.class);
		System.out.println("RESPONSE\n" + response2.getBody());

		final ObjectMapper mapper = new ObjectMapper();

		final String expected = mapper.writeValueAsString(fat);
		System.out.println("EXPECTED\n" + expected);
		JSONAssert.assertEquals(expected, response.getBody(), false);

		open("/soaplab/ui/fats");
		log.info("### opened browser with url " + WebDriverRunner.url());

		VaadinUtils.waitUntilPageLoaded();

		log.info("### test run success " + getTestName());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private static void configureSelenideBaseSetup() {
		Configuration.browser = Browsers.FIREFOX;
		final FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", Locale.getDefault().getLanguage());
		final FirefoxOptions browserOptions = new FirefoxOptions().setProfile(profile).addArguments("--no-sandbox")
				.addArguments("--disable-dev-shm-usage").addArguments("--headless=new");
		;
		Configuration.headless = true;
		Configuration.browserCapabilities = browserOptions;
		Configuration.reportsFolder = "target/failsafe-reports";
		Configuration.timeout = Duration.ofSeconds(5).toMillis();
		Configuration.clickViaJs = true;
		Configuration.fastSetValue = true;
	}

	private static void configureDatabaseFolder(Environment environment) {
		final String databaseFolderProperty = environment.getProperty("microstream.store.location");
		if (databaseFolder == null) {
			databaseFolder = new File(databaseFolderProperty);
			log.info("Setting database folder to " + databaseFolder);
		}
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

	protected String getTestName() {
		return testInfo.getTestMethod().get().getName();
	}

	private synchronized static void removeDatabaseFolder() {
		log.info("[....] Removing testdatabase: " + databaseFolder);
		try {
			if (databaseFolder != null) {
				// also remove old testdatabase files&folders
				final String[] testDatabasesFolders = databaseFolder.getParentFile()
						.list((dir, name) -> name.startsWith("test-"));
				for (int i = 0; i < testDatabasesFolders.length; i++) {
					try {
						final File fileToDelete = new File(databaseFolder.getParentFile(), testDatabasesFolders[i]);
						FileUtils.forceDelete(fileToDelete);
						log.info("[DONE] Removing testdatabase: " + fileToDelete);
					} catch (final Exception e) {
						log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolder, e);
					}
				}
				databaseFolder = null;
			}
		} catch (final Exception e) {
			log.error("[ERROR] Error occured while removing testdatabase: " + databaseFolder, e);
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
