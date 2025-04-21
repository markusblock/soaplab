package org.soaplab.ui.views;

import java.math.BigDecimal;

import org.soaplab.domain.Price;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public final class StringToPriceValueConverter implements Converter<String, Price> {

	private static final long serialVersionUID = 1L;

	private MyStringToBigDecConverter stringToBigDecConverter;

	public StringToPriceValueConverter() {
		stringToBigDecConverter = new MyStringToBigDecConverter(MyStringToBigDecConverter.ALWAYS_2_DECIMAL_PLACES);
	}

	public Result<Price> convertToModel(String value, ValueContext context) {
		return stringToBigDecConverter.convertToModel(value, context).map(number -> new Price((BigDecimal) number));
	}

	@Override
	public String convertToPresentation(Price value, ValueContext context) {
		if (value == null) {
			return null;
		}

		return stringToBigDecConverter.convertToPresentation(value.getValue(), context);
	}
}