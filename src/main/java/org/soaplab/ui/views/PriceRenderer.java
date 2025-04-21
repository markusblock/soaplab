package org.soaplab.ui.views;

import java.util.Objects;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.Price;

import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

public class PriceRenderer<T extends NamedEntity> extends BasicRenderer<T, Price> {

	private static final long serialVersionUID = 1L;

	private StringToPriceValueConverter converter = new StringToPriceValueConverter();

	public PriceRenderer(ValueProvider<T, Price> valueProvider) {
		super(valueProvider);
	}

	@Override
	public String getFormattedValue(Price object) {
		if (Objects.isNull(object)) {
			return "";
		}
		String stringPresentation = converter.convertToPresentation(object, new ValueContext());
		return stringPresentation + " €";
	}
}
