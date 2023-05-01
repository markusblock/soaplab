package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.$;

import java.time.Duration;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaadinUtils {

	public static Condition disabled() {
		return Condition.attribute("disabled");
	}

	public static Condition selected() {
		return Condition.attribute("selected");
	}

	public static void clickOnElement(By selector) {
		$(selector).scrollIntoView(true).shouldNotBe(disabled()).click();
	}

	public static void waitUntilPageLoaded() {
		// message: The frontend development build has not yet finished. Please wait...
		if ($(".flex-center").$(".message").isDisplayed()) {
			log.info(
					"waiting for message 'The frontend development build has not yet finished. Please wait...' to disappear ...");
			$(".flex-center").$(".message").shouldNotBe(Condition.visible, Duration.ofSeconds(30));
		}
		if ($(".v-loading-indicator").isDisplayed()) {
			log.info("waiting for loading indicator to disappear ...");
			Selenide.Wait().withTimeout(Duration.ofSeconds(30))
					.until(d -> Float.parseFloat($(".v-loading-indicator").getCssValue("opacity")) <= 0);
		}
		if ($(".v-system-error").$(".caption").has(Condition.text("Cookies disabled"))) {
			log.info("error message 'Cookies disabled appeared... Refreshing page...'");
			Selenide.refresh();
			$(".v-system-error").shouldNotBe(Condition.visible, Duration.ofSeconds(30));
		}

		log.info("waiting for soaplab.id to become visible");
		$(Selectors.byId("soaplab.id")).shouldBe(Condition.visible, Duration.ofSeconds(30));

	}
}
