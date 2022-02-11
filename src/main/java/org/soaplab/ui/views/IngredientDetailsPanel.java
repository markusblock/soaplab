package org.soaplab.ui.views;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public abstract class IngredientDetailsPanel<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private FormLayout content;

	private Binder<T> binder;

	public IngredientDetailsPanel() {
		super();

		content = new FormLayout();
		add(content);

		TextField id = new TextField(getTranslation("domain.ingredient.id"));
		content.add(id);

		TextField name = new TextField(getTranslation("domain.ingredient.name"));
		content.add(name);

		TextField inci = new TextField(getTranslation("domain.ingredient.inci"));
		content.add(inci);

		binder = new Binder<>();
		binder.forField(id).bindReadOnly(ingredient -> ingredient.getId().toString());
		binder.forField(name).bind(T::getName, T::setName);
		binder.forField(inci).bind(T::getInci, T::setInci);

	}

	protected void addProperty(String propertyName) {
		addProperty(propertyName, null);
	}

	protected void addProperty(String propertyName, String propertyValue) {
		TextField textField = new TextField(propertyName);
		if (propertyValue == null) {
			textField.clear();
		} else {
			textField.setValue(propertyValue);
		}
		content.add(textField);
	}

	public void setData(T ingredient) {
		binder.readBean(ingredient);
	}

}
