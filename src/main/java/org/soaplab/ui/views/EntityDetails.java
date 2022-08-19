package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Weight;
import org.springframework.util.Assert;

import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class EntityDetails<T extends NamedEntity> extends Div
		implements BeforeEnterObserver, BeforeLeaveObserver {

	private static final long serialVersionUID = 1L;

	@Getter(value = AccessLevel.PROTECTED)
	private VerticalLayout content;
	private FormLayout detailsPanel;

	private Binder<T> binder;
	private List<HasEnabled> editablePropertyFields;

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
			saveInternal(callback);
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

		setActionButtonVisibility(false);
	}

	private void setActionButtonVisibility(boolean editMode) {
		editButton.setVisible(!editMode);
		addButton.setVisible(!editMode);
		removeButton.setVisible(!editMode);
		saveButton.setVisible(editMode);
		cancelButton.setVisible(editMode);
	}

	private void saveInternal(EntityViewDetailsControllerCallback<T> callback) {

		preSave();

		binder.writeBeanIfValid(entity);
		callback.saveEntity(entity);
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void preSave() {
	}

	private void enterEditModeInternal() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
		setActionButtonVisibility(true);

		enterEditMode();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void enterEditMode() {
	}

	private void leaveEditModeInternal() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
		editButton.setEnabled(entity != null);
		removeButton.setEnabled(entity != null);
		setActionButtonVisibility(false);

		leaveEditMode();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void leaveEditMode() {
	}

	private void setEntityInternal(T entity) {
		this.entity = entity;
		binder.readBean(entity);
		editButton.setEnabled(entity != null);
		removeButton.setEnabled(entity != null);

		setEntity(entity);
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 * 
	 * @param entity the entity to set, or <code>null</code> to clear the fields
	 */
	protected void setEntity(T entity) {
	}

	public void showEntity(T entity) {
		setEntityInternal(entity);
		leaveEditModeInternal();
	}

	public void editEntity(T newEntity) {
		Assert.notNull(newEntity, "Empty entity not editable");
		setEntityInternal(newEntity);
		enterEditModeInternal();
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

	protected void addPropertyTextArea(String id, ValueProvider<T, String> getter, Setter<T, String> setter) {
		TextArea propertyField = createPropertyTextArea(id);
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).bind(getter, setter);
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

	protected void addPropertyWeightField(String id, ValueProvider<T, Weight> getter, Setter<T, Weight> setter) {
		TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("g")));
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToWeightValueConverter())
				.bind(getter, setter);
	}

	protected void addPropertyPercentageField(String id, ValueProvider<T, Percentage> getter,
			Setter<T, Percentage> setter) {
		TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("%")));
		editablePropertyFields.add(propertyField);
		detailsPanel.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToPercentageConverter())
				.bind(getter, setter);
	}

	private TextField createPropertyTextField(String id) {
		TextField propertyField = new TextField();
		propertyField.setId(id);
		propertyField.setWidthFull();
		propertyField.setEnabled(false);
		return propertyField;
	}

	private TextArea createPropertyTextArea(String id) {
		TextArea textArea = new TextArea();
		textArea.setId(id);
		textArea.setWidthFull();
		textArea.setEnabled(false);
		return textArea;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		leaveEditModeInternal();
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		leaveEditModeInternal();
	}

}
