package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.$;

import java.time.Duration;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;

public class VaadinUtils {

	public static Condition disabled() {
		return Condition.attribute("disabled");
	}

	public static Condition selected() {
		return Condition.attribute("selected");
	}

	public static void clickOnElement(By selector) {
		$(selector).scrollTo().shouldNotBe(disabled()).click();
	}

	public static void waitUntilPageLoaded() {
		// message: The frontend development build has not yet finished. Please wait...
//		$(".flex-center").$(".message").shouldNotBe(Condition.visible, Duration.ofSeconds(30));
//		Selenide.Wait().withTimeout(Duration.ofSeconds(10))
//				.until(d -> Float.parseFloat($(".v-loading-indicator").getCssValue("opacity")) <= 0);
		$(Selectors.byId("soaplab.id")).shouldBe(Condition.visible, Duration.ofSeconds(60));

	}
}
