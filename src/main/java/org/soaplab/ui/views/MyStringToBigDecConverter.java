package org.soaplab.ui.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class MyStringToBigDecConverter extends StringToBigDecimalConverter {

	private static final long serialVersionUID = 1L;

	public MyStringToBigDecConverter(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected NumberFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.##", new DecimalFormatSymbols(locale));
		decimalFormat.setParseBigDecimal(true);
		decimalFormat.setMaximumFractionDigits(2);
		return decimalFormat;
	}
}