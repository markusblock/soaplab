package org.soaplab.ui.fat;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;

public class VaadinUtils {

	public static Condition disabled() {
		return Condition.attribute("disabled");
	}

	public static Condition selected() {
		return Condition.attribute("selected");
	}

	public static void clickOnElement(By selector) {
		$(selector).shouldNotBe(disabled()).click();
	}
}
