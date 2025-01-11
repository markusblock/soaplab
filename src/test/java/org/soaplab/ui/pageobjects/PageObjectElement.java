package org.soaplab.ui.pageobjects;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static org.soaplab.ui.fat.VaadinUtils.disabled;
import static org.soaplab.ui.fat.VaadinUtils.readonly;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.soaplab.ui.fat.VaadinUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageObjectElement {

	private final By locator;

	public PageObjectElement shouldBeEmpty() {
		$(locator).shouldBe(empty);
		return this;
	}

	public PageObjectElement shouldNotBeEmpty() {
		$(locator).shouldNotBe(empty);
		return this;
	}

	public PageObjectElement shouldBeReadOnly() {
		$(locator).shouldBe(readonly());
		return this;
	}

	public PageObjectElement shouldBeDisabled() {
		$(locator).shouldBe(disabled());
		return this;
	}

	public PageObjectElement shouldBeVisible() {
		$(locator).shouldBe(visible);
		return this;
	}

	public boolean isVisible() {
		return $(locator).is(visible);
	}

	public PageObjectElement shouldBeHidden() {
		$(locator).shouldBe(hidden);
		return this;
	}

	public boolean isEditable() {
		return $(locator).has(not(disabled()));
	}

	public PageObjectElement shouldBeEditable() {
		return shouldBeEnabled();
	}

	public PageObjectElement shouldBeEnabled() {
		$(locator).shouldNotBe(disabled());
		return this;
	}

	public PageObjectElement shouldHaveValue(String value) {
		$(locator).$(byTagName("input")).shouldHave(value(value));
		return this;
	}

	public PageObjectElement shouldHaveValue(Integer value) {
		return shouldHaveValue(value.toString());
	}

	public PageObjectElement shouldHaveValue(UUID value) {
		return shouldHaveValue(value.toString());
	}

	public PageObjectElement shouldHaveValue(BigDecimal value) {
		return shouldHaveValue(DecimalFormat.getInstance().format(value));
	}

	public PageObjectElement setValue(String value) {
		$(locator).$(byTagName("input")).scrollIntoView(true).setValue(value);
		if (ObjectUtils.isEmpty(value)) {
			shouldBeEmpty();
		} else {
			shouldHaveValue(value);
		}
		return this;
	}

	public PageObjectElement clearValue() {
		$(locator).$(byTagName("input")).scrollIntoView(true).clear();
		return this;
	}

	public PageObjectElement appendValue(String value) {
		$(locator).$(byTagName("input")).scrollIntoView(true).append(value);
		return this;
	}

	public PageObjectElement setValue(Integer value) {
		return setValue(value.toString());
	}

	public PageObjectElement setValue(BigDecimal value) {
		return setValue(DecimalFormat.getInstance().format(value.doubleValue()));
	}

	public PageObjectElement click() {
		$(locator).scrollIntoCenter();
		VaadinUtils.clickOnElement(locator);
		return this;
	}

	public PageObjectElement doubleClick() {
		$(locator).scrollIntoCenter();
		VaadinUtils.doubleClickOnElement(locator);
		return this;
	}

	public PageObjectElement press(CharSequence... keysToPress) {
		$(locator).$(byTagName("input")).scrollIntoView(true).press(keysToPress);
		return this;
	}

	public PageObjectElement pressEnter() {
		$(locator).$(byTagName("input")).scrollIntoView(true).pressEnter();
		return this;
	}

	public PageObjectElement pressEscape() {
		$(locator).$(byTagName("input")).scrollIntoView(true).pressEscape();
		return this;
	}

	public WebElement toWebElement() {
		return $(locator).toWebElement();
	}
}