package org.soaplab.ui.views;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.soaplab.domain.KOH;
import org.soaplab.domain.Percentage;
import org.soaplab.ui.views.PercentageRenderer;

class PercentageRendererTest {

	private PercentageRenderer<KOH> renderer;

	@BeforeEach
	void beforeEach() {
		Locale.setDefault(new Locale("de", "DE"));
		renderer = new PercentageRenderer<KOH>(KOH::getKohPurity);
	}

	@Test
	void nullReturnsEmptyString() {
		assertThat(renderer.getFormattedValue(null)).isEmpty();
	}

	@Test
	void zeroReturnsFormattedPriceString() {
		assertThat(renderer.getFormattedValue(Percentage.of(0))).isEqualTo("0 %");
	}

	@Test
	void oneDigitReturnsTwoDigitsAfter() {
		assertThat(renderer.getFormattedValue(Percentage.of(1))).isEqualTo("1 %");
	}

	@Test
	void afterDigitArePreserved() {
		assertThat(renderer.getFormattedValue(Percentage.of(1.23))).isEqualTo("1,23 %");
	}

	@Test
	void thousandDE() {
		assertThat(renderer.getFormattedValue(Percentage.of(10101.23))).isEqualTo("10.101,23 %");
	}

	@Test
	void thousandEN() {
		Locale.setDefault(new Locale("en", "EN"));
		assertThat(renderer.getFormattedValue(Percentage.of(10101.23))).isEqualTo("10,101.23 %");
	}

}
