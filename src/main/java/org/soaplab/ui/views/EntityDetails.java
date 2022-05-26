package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.soaplab.domain.NamedEntity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class EntityDetails<T extends NamedEntity> extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;
	private FormLayout detailsPanel;

	private Binder<T> binder;
	private List<TextField> editablePropertyFields;

	private Button editButton;
	private Button addButton;
	private Button removeButton;
	private Button saveButton;
	private Button cancelButton;

	private T entity;

	protected EntityDetails(EntityViewDetailsControllerCallback<T> callback) {
		super();

		editablePropertyFields = new ArrayList<>();

		binder = new Binder<>();

		content = new VerticalLayout();
		add(content);

		HorizontalLayout buttonPanel = new HorizontalLayout();

		addButton = new Button();
		addButton.setId("entitydetails.add");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(event -> {
			callback.createNewEntity();
		});
		buttonPanel.add(addButton);

		removeButton = new Button();
		removeButton.setId("entitydetails.remove");
		removeButton.setIcon(VaadinIcon.MINUS.create());
		removeButton.addClickListener(event -> {
			callback.deleteEntity(entity);
		});
		buttonPanel.add(removeButton);

		editButton = new Button();
		editButton.setId("entitydetails.edit");
		editButton.setIcon(VaadinIcon.PENCIL.create());
		editButton.addClickListener(event -> {
			callback.editEntity(entity);
		});
		buttonPanel.add(editButton);

		saveButton = new Button();
		saveButton.setId("entitydetails.save");
		saveButton.setIcon(VaadinIcon.CHECK.create());
		saveButton.addClickListener(event -> {
			binder.writeBeanIfValid(entity);
			callback.saveEntity(entity);
		});
		buttonPanel.add(saveButton);

		cancelButton = new Button();
		cancelButton.setId("entitydetails.cancel");
		cancelButton.setIcon(VaadinIcon.CLOSE.create());
		cancelButton.addClickListener(event -> {
			callback.cancelEditMode();
		});
		buttonPanel.add(cancelButton);

		content.add(buttonPanel);

		detailsPanel = new FormLayout();
		detailsPanel.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1));
		content.add(detailsPanel);

		TextField idField = createPropertyTextField("domain.entity.id");
		detailsPanel.addFormItem(idField, getTranslation("domain.entity.id"));
		binder.forField(idField).bindReadOnly(ingred -> Objects.toString(ingred.getId(), ""));

		addPropertyStringField("domain.entity.name", T::getName, T::setName, true);

	}

	private void enterEditMode() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
		editButton.setVisible(false);
		addButton.setVisible(false);
		removeButton.setVisible(false);
		saveButton.setVisible(true);
		cancelButton.setVisible(true);
	}

	private void leaveEditMode() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
		editButton.setVisible(true);
		editButton.setEnabled(entity != null);
		addButton.setVisible(true);
		removeButton.setVisible(true);
		removeButton.setEnabled(entity != null);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
	}

	private void setEntity(T entity) {
		this.entity = entity;
		binder.readBean(entity);
		editButton.setEnabled(entity != null);
		removeButton.setEnabled(entity != null);
	}

	public void showEntity(T entity) {
		setEntity(entity);
		leaveEditMode();
	}

	public void editEntity(T newEntity) {
		setEntity(newEntity);
		enterEditMode();
	}

	protected void addPropertyStringField(String id, ValueProvider<T, String> getter, Setter<T, String> setter,
			boolean required) {
		TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		BindingBuilder<T, String> bindingBuilder = binder.forField(propertyField);
		if (required) {
			bindingBuilder.asRequired();
		}
		bindingBuilder.bind(getter, setter);
	}

	protected void addPropertyIntegerField(String id, ValueProvider<T, Integer> getter, Setter<T, Integer> setter) {
		TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToIntegerConverter(""))
				.bind(getter, setter);
	}

	protected void addPropertyBigDecimalField(String id, ValueProvider<T, BigDecimal> getter,
			Setter<T, BigDecimal> setter) {
		TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new MyStringToBigDecConverter(""))
				.bind(getter, setter);
	}

	private TextField createPropertyTextField(String id) {
		TextField propertyField = new TextField();
		propertyField.setId(id);
		propertyField.setWidthFull();
		propertyField.setEnabled(false);
		return propertyField;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		leaveEditMode();
	}

}
