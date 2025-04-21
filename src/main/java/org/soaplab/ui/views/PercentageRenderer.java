package org.soaplab.ui.views;

import java.util.Objects;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.Percentage;

import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

public class PercentageRenderer<T extends NamedEntity> extends BasicRenderer<T, Percentage> {

	private static final long serialVersionUID = 1L;

	private StringToPercentageConverter converter = new StringToPercentageConverter();

	public PercentageRenderer(ValueProvider<T, Percentage> valueProvider) {
		super(valueProvider);
	}

	@Override
	protected String getFormattedValue(Percentage object) {
		if (Objects.isNull(object)) {
			return "";
		}
		String stringPresentation = converter.convertToPresentation(object, new ValueContext());
		return stringPresentation + " %";
	}
}
