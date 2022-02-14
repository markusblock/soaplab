package org.soaplab.ui.views;

import java.math.BigDecimal;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;

public abstract class IngredientDetailsPanel<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private FormLayout content;

	private Binder<T> binder;

	public IngredientDetailsPanel() {
		super();

		content = new FormLayout();
		add(content);

		binder = new Binder<>();

		TextField idField = new TextField(getTranslation("domain.ingredient.id"));
		content.add(idField);
		binder.forField(idField).bindReadOnly(ingredient -> ingredient.getId().toString());

		addPropertyStringField("domain.ingredient.name", T::getName, T::setName);
		addPropertyStringField("domain.ingredient.inci", T::getInci, T::setInci);

	}

	public void setData(T ingredient) {
		binder.readBean(ingredient);
	}

	protected void addPropertyStringField(String messageId, ValueProvider<T, String> getter, Setter<T, String> setter) {
		TextField propertyField = new TextField(getTranslation(messageId));
		content.add(propertyField);
		binder.forField(propertyField).bind(getter, setter);
	}

	protected void addPropertyIntegerField(String messageId, ValueProvider<T, Integer> getter,
			Setter<T, Integer> setter) {
		TextField propertyField = new TextField(getTranslation(messageId));
		content.add(propertyField);
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToIntegerConverter(""))
				.bind(getter, setter);
	}

	protected void addPropertyBigDecimalField(String messageId, ValueProvider<T, BigDecimal> getter,
			Setter<T, BigDecimal> setter) {
		TextField propertyField = new TextField(getTranslation(messageId));
		content.add(propertyField);
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToBigDecimalConverter(""))
				.bind(getter, setter);
	}

}
