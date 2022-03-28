package org.soaplab.ui.fat;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selenide.$;
import static org.soaplab.ui.fat.VaadinUtils.disabled;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.openqa.selenium.By;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageObjectElement {

	private By locator;

	public PageObjectElement shouldBeEmpty() {
		$(locator).shouldBe(empty);
		return this;
	}

	public PageObjectElement shouldNotBeEmpty() {
		$(locator).shouldNotBe(empty);
		return this;
	}

	public PageObjectElement shouldBeReadOnly() {
		$(locator).shouldBe(disabled());
		return this;
	}

	public PageObjectElement shouldBeEditable() {
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

	public PageObjectElement shouldHaveValue(BigDecimal value) {
		return shouldHaveValue(DecimalFormat.getInstance().format(value.doubleValue()));
	}

	public PageObjectElement setValue(String value) {
		$(locator).$(byTagName("input")).setValue(value);
		return this;
	}

	public PageObjectElement setValue(Integer value) {
		return setValue(value.toString());
	}

	public PageObjectElement setValue(BigDecimal value) {
		return setValue(DecimalFormat.getInstance().format(value.doubleValue()));
	}
}