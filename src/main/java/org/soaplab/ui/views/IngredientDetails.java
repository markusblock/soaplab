package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.soaplab.domain.Ingredient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class IngredientDetails<T extends Ingredient> extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;
	private FormLayout detailsPanel;

	private Binder<T> binder;
	private List<TextField> editablePropertyFields;

	private Button editButton;
	private Button saveButton;
	private Button cancelButton;

	private T ingredient;

	private IngredientsViewControllerCallback<T> controllerCallback;

	public IngredientDetails(IngredientsViewControllerCallback<T> controllerCallback) {
		super();
		this.controllerCallback = controllerCallback;

		editablePropertyFields = new ArrayList<>();

		content = new VerticalLayout();
		add(content);

		HorizontalLayout buttonPanel = new HorizontalLayout();
		editButton = new Button();
		editButton.setIcon(VaadinIcon.PENCIL.create());
		editButton.addClickListener(event -> {
			enterEditMode();
		});
		buttonPanel.add(editButton);

		saveButton = new Button();
		saveButton.setIcon(VaadinIcon.CHECK.create());
		saveButton.addClickListener(event -> {
			leaveEditMode(true);
			controllerCallback.onSaveIngredient(ingredient);
		});
		buttonPanel.add(saveButton);

		cancelButton = new Button();
		cancelButton.setIcon(VaadinIcon.CLOSE.create());
		cancelButton.addClickListener(event -> {
			leaveEditMode(false);
		});
		buttonPanel.add(cancelButton);

		content.add(buttonPanel);

		detailsPanel = new FormLayout();
		detailsPanel.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1));
		content.add(detailsPanel);

		binder = new Binder<>();

		TextField idField = createPropertyTextField();
		detailsPanel.addFormItem(idField, getTranslation("domain.ingredient.id"));
		binder.forField(idField).bindReadOnly(ingredient -> Objects.toString(ingredient.getId(), ""));

		addPropertyStringField("domain.ingredient.name", T::getName, T::setName);
		addPropertyStringField("domain.ingredient.inci", T::getInci, T::setInci);

	}

	private void enterEditMode() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
		editButton.setVisible(false);
		saveButton.setVisible(true);
		cancelButton.setVisible(true);
	}

	private void leaveEditMode(boolean save) {
		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
		editButton.setVisible(true);
		editButton.setEnabled(ingredient != null);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);

		if (save) {
			saveData();
		}
	}

	public void setIngredient(T ingredient) {
		this.ingredient = ingredient;
		binder.readBean(ingredient);
		editButton.setEnabled(ingredient != null);
	}

	public void saveData() {
		binder.writeBeanIfValid(ingredient);
	}

	protected void addPropertyStringField(String messageId, ValueProvider<T, String> getter, Setter<T, String> setter) {
		TextField propertyField = createPropertyTextField();
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(messageId));
		binder.forField(propertyField).bind(getter, setter);
	}

	protected void addPropertyIntegerField(String messageId, ValueProvider<T, Integer> getter,
			Setter<T, Integer> setter) {
		TextField propertyField = createPropertyTextField();
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(messageId));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToIntegerConverter(""))
				.bind(getter, setter);
	}

	protected void addPropertyBigDecimalField(String messageId, ValueProvider<T, BigDecimal> getter,
			Setter<T, BigDecimal> setter) {
		TextField propertyField = createPropertyTextField();
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(messageId));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToBigDecimalConverter(""))
				.bind(getter, setter);
	}

	private TextField createPropertyTextField() {
		TextField propertyField = new TextField();
		propertyField.setWidthFull();
		propertyField.setEnabled(false);
		return propertyField;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		leaveEditMode(false);
	}

	public void createIngredient(T newEntity) {
		setIngredient(newEntity);
		enterEditMode();
	}

}
