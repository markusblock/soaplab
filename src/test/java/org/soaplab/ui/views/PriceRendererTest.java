package org.soaplab.ui.views;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Price;

class PriceRendererTest {

	private PriceRenderer<Ingredient> renderer;

	@BeforeEach
	void beforeEach() {
		Locale.setDefault(new Locale("de", "DE"));
		renderer = new PriceRenderer<Ingredient>(Ingredient::getCost);
	}

	@Test
	void nullReturnsEmptyString() {
		assertThat(renderer.getFormattedValue(null)).isEmpty();
	}

	@Test
	void zeroReturnsFormattedPriceString() {
		assertThat(renderer.getFormattedValue(Price.of(0))).isEqualTo("0,00 €");
	}

	@Test
	void oneDigitReturnsTwoDecimalPlaces() {
		assertThat(renderer.getFormattedValue(Price.of(1))).isEqualTo("1,00 €");
	}

	@Test
	void decimalPlacesArePreserved() {
		assertThat(renderer.getFormattedValue(Price.of(1.23))).isEqualTo("1,23 €");
	}

	@Test
	void thousand() {
		assertThat(renderer.getFormattedValue(Price.of(10101.23))).isEqualTo("10.101,23 €");
	}

	@Test
	void thousandEN() {
		Locale.setDefault(new Locale("en", "EN"));
		assertThat(renderer.getFormattedValue(Price.of(10101.23))).isEqualTo("10,101.23 €");
	}

}
