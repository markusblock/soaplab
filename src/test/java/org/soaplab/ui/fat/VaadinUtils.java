package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.$;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebElementCondition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaadinUtils {

	public static WebElementCondition disabled() {
		return Condition.attribute("disabled");
	}

	public static WebElementCondition selected() {
		return Condition.attribute("selected");
	}

	public static WebElementCondition editable() {
		return Condition.attribute("editable");
	}

	public static WebElementCondition readonly() {
		return Condition.attribute("readonly");
	}

	public static void clickOnElement(By selector) {
		$(selector).scrollIntoView(true).shouldNotBe(disabled()).click();
	}

	public static void doubleClickOnElement(By selector) {
//		$(selector).doubleClick();
//		final String jsDoubleClick = "var target = arguments[0];                                 "
//				+ "var offsetX = arguments[1];                                "
//				+ "var offsetY = arguments[2];                                "
//				+ "var rect = target.getBoundingClientRect();                 "
//				+ "var cx = rect.left + (offsetX || (rect.width / 2));        "
//				+ "var cy = rect.top + (offsetY || (rect.height / 2));        "
//				+ "                                                           "
//				+ "emit('mousedown', {clientX: cx, clientY: cy, buttons: 1}); "
//				+ "emit('mouseup',   {clientX: cx, clientY: cy});             "
//				+ "emit('mousedown', {clientX: cx, clientY: cy, buttons: 1}); "
//				+ "emit('mouseup',   {clientX: cx, clientY: cy});             "
//				+ "emit('click',     {clientX: cx, clientY: cy, detail: 2});  "
//				+ "                                                           "
//				+ "function emit(name, init) {                                "
//				+ "target.dispatchEvent(new MouseEvent(name, init));        "
//				+ "}                                                          ";

		final WebElement webElement = $(selector).toWebElement();
		final WebDriver webDriver = Selenide.webdriver().object();

//		new Actions(webDriver).moveToElement(webElement, 0, 0).perform();
//		((JavascriptExecutor) webDriver).executeScript(jsDoubleClick, webElement, 0, 0);

		new Actions(webDriver).moveToElement(webElement).doubleClick().perform();
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
