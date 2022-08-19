package org.soaplab.ui.views;

import java.math.BigDecimal;

import org.soaplab.domain.Percentage;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public final class StringToPercentageConverter implements Converter<String, Percentage> {

	private static final long serialVersionUID = 1L;

	private MyStringToBigDecConverter stringToBigDecConverter;

	public StringToPercentageConverter() {
		stringToBigDecConverter = new MyStringToBigDecConverter("");
	}

	public Result<Percentage> convertToModel(String value, ValueContext context) {
		return stringToBigDecConverter.convertToModel(value, context)
				.map(number -> new Percentage((BigDecimal) number));
	}

	@Override
	public String convertToPresentation(Percentage value, ValueContext context) {
		if (value == null) {
			return null;
		}

		return stringToBigDecConverter.convertToPresentation(value.getNumber(), context);
	}
}