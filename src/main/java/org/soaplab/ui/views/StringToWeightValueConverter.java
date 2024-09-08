package org.soaplab.ui.views;

import java.math.BigDecimal;

import org.soaplab.domain.Weight;
import org.soaplab.domain.WeightUnit;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public final class StringToWeightValueConverter implements Converter<String, Weight> {

	private static final long serialVersionUID = 1L;

	private MyStringToBigDecConverter stringToBigDecConverter;

	public StringToWeightValueConverter() {
		stringToBigDecConverter = new MyStringToBigDecConverter();
	}

	public Result<Weight> convertToModel(String value, ValueContext context) {
		return stringToBigDecConverter.convertToModel(value, context)
				.map(number -> new Weight((BigDecimal) number, WeightUnit.GRAMS));
	}

	@Override
	public String convertToPresentation(Weight value, ValueContext context) {
		if (value == null) {
			return null;
		}

		return stringToBigDecConverter.convertToPresentation(value.getWeight(), context);
	}
}