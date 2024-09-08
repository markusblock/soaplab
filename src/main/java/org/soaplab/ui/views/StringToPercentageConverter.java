package org.soaplab.ui.views;

import java.math.BigDecimal;

import org.soaplab.domain.Percentage;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StringToPercentageConverter implements Converter<String, Percentage> {

	private static final long serialVersionUID = 1L;

	private MyStringToBigDecConverter stringToBigDecConverter;

	public StringToPercentageConverter() {
		stringToBigDecConverter = new MyStringToBigDecConverter();
	}

	@Override
	public Result<Percentage> convertToModel(String value, ValueContext context) {
		Result<Percentage> result = stringToBigDecConverter.convertToModel(value, context)
				.map(number -> new Percentage((BigDecimal) number));

		log.debug("Converting presentation %s to model %s".formatted(value, result));
		return result;
	}

	@Override
	public String convertToPresentation(Percentage value, ValueContext context) {
		if (value == null) {
			return null;
		}

		String s = stringToBigDecConverter.convertToPresentation(value.getNumber(), context);
		log.debug("Converting model %s to presentation %s".formatted(value, s));
		return s;
	}
}