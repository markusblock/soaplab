package org.soaplab.ui.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class MyStringToBigDecConverter extends StringToBigDecimalConverter {

	private static final long serialVersionUID = 1L;

	// 1 / 0 / 1,23 / 0,01
	public static String DEFAULT_NUMBER_FORMAT_PATTERN = "#,##0.##";

	// 1,00 / 0,00 / 1,23 / 0,01
	public static String ALWAYS_2_DECIMAL_PLACES = "#,##0.00";

	private String numberFormatPattern;

	public MyStringToBigDecConverter() {
		this(DEFAULT_NUMBER_FORMAT_PATTERN);
	}

	public MyStringToBigDecConverter(String numberFormatPattern) {
		super("Error during conversion");
		this.numberFormatPattern = numberFormatPattern;
	}

	@Override
	protected NumberFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		DecimalFormat decimalFormat = new DecimalFormat(numberFormatPattern, new DecimalFormatSymbols(locale));
		decimalFormat.setParseBigDecimal(true);
		decimalFormat.setMaximumFractionDigits(2);
		return decimalFormat;
	}
}