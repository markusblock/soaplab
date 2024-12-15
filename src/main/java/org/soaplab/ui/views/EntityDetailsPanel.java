package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;
import org.soaplab.domain.Weight;
import org.soaplab.repository.EntityRepository;
import org.springframework.util.Assert;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Div;
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

import lombok.extern.slf4j.Slf4j;

//if entity is null -> fields enabled false
//if table is in editMode details should leave editMode
//esc reset field to old value
//table for fields (key - value+unit)
@Slf4j
public abstract class EntityDetailsPanel<T extends NamedEntity> extends Div
		implements BeforeEnterObserver, BeforeLeaveObserver {

	private static final long serialVersionUID = 1L;

	private final VerticalLayout content;
	private final FormLayout commonEntityDetailsSection;
	private final FormLayout propertySection;

	private final Binder<T> binder;
	private final List<HasEnabled> editablePropertyFields;

	private T entity;
	private boolean escapePressed;

	private EntityDetailsListener<T> listener;

	protected EntityDetailsPanel(EntityDetailsListener<T> listener) {
		super();
		this.listener = listener;

		editablePropertyFields = new ArrayList<>();

		binder = new Binder<>();

		content = new VerticalLayout();
		add(content);

		commonEntityDetailsSection = new FormLayout();
		commonEntityDetailsSection.setId("entitydetails.section.common");
		commonEntityDetailsSection.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1, LabelsPosition.ASIDE));
		content.add(commonEntityDetailsSection);

		final TextField idField = createPropertyTextField("domain.entity.id");
		commonEntityDetailsSection.addFormItem(idField, getTranslation("domain.entity.id"));
		binder.forField(idField).bindReadOnly(ingred -> Objects.toString(ingred.getId(), ""));

		addPropertyStringField("domain.entity.name", T::getName, T::setName, true);

		propertySection = new FormLayout();
		propertySection.setId("entitydetails.section.properties");
		propertySection.setResponsiveSteps(
				// Use two columns by default
				new ResponsiveStep("0", 3, LabelsPosition.TOP));
		content.add(propertySection);
	}

	protected void addContent(Component component) {
		content.add(component);
	}

	protected void addEntityDetail(Component component) {
		commonEntityDetailsSection.add(component);
	}

	protected void addEntityProperty(Component component) {
		commonEntityDetailsSection.add(component);
	}

	protected void addPropertyBigDecimalField(String id, ValueProvider<T, BigDecimal> getter,
			Setter<T, BigDecimal> setter) {
		final TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new MyStringToBigDecConverter())
				.bind(getter, setter);
	}

	protected void addPropertyIntegerField(String id, ValueProvider<T, Integer> getter, Setter<T, Integer> setter) {
		final TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToIntegerConverter(""))
				.bind(getter, setter);
	}

	protected void addPropertyPercentageField(String id, ValueProvider<T, Percentage> getter,
			Setter<T, Percentage> setter) {
		final TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("%")));
		editablePropertyFields.add(propertyField);
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToPercentageConverter())
				.bind(getter, setter);
	}

	protected void addPropertyPriceField(String id, ValueProvider<T, Price> getter, Setter<T, Price> setter) {
		final TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("€")));
		editablePropertyFields.add(propertyField);
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToPriceValueConverter())
				.bind(getter, setter);
	}

	protected void addPropertyPriceFieldReadOnly(String id, ValueProvider<T, Price> getter) {
		final TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("€")));
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToPriceValueConverter())
				.bindReadOnly(getter);
	}

	protected void addPropertyStringField(String id, ValueProvider<T, String> getter, Setter<T, String> setter,
			boolean required) {
		final TextField propertyField = createPropertyTextField(id);
		editablePropertyFields.add(propertyField);
		commonEntityDetailsSection.addFormItem(propertyField, getTranslation(id));
		final BindingBuilder<T, String> bindingBuilder = binder.forField(propertyField);
		if (required) {
			bindingBuilder.asRequired();
		}
		bindingBuilder.bind(getter, setter);
	}

	protected void addPropertyTextArea(String id, ValueProvider<T, String> getter, Setter<T, String> setter) {
		final TextArea propertyField = createPropertyTextArea(id);
		editablePropertyFields.add(propertyField);
		commonEntityDetailsSection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).bind(getter, setter);
	}

	protected void addPropertyWeightField(String id, ValueProvider<T, Weight> getter, Setter<T, Weight> setter) {
		final TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("g")));
		editablePropertyFields.add(propertyField);
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToWeightValueConverter())
				.bind(getter, setter);
	}

	protected void addPropertyWeightFieldReadOnly(String id, ValueProvider<T, Weight> getter) {
		final TextField propertyField = createPropertyTextField(id);
		propertyField.setSuffixComponent(new Div(new Text("g")));
		propertySection.addFormItem(propertyField, getTranslation(id));
		binder.forField(propertyField).withNullRepresentation("").withConverter(new StringToWeightValueConverter())
				.bindReadOnly(getter);
	}

	protected <ENTITY extends NamedEntity> void addEntitySelector(String id, ValueProvider<T, ENTITY> getter,
			Setter<T, ENTITY> setter, EntityRepository<ENTITY> repository) {
		final ComboBox<ENTITY> entitySelector = new ComboBox<ENTITY>();
		entitySelector.setWidthFull();
		entitySelector.setItems(repository.findAll());
		entitySelector.setItemLabelGenerator(entity -> entity.getName());
		editablePropertyFields.add(entitySelector);
		commonEntityDetailsSection.addFormItem(entitySelector, getTranslation(id));
		binder.forField(entitySelector).bind(getter, setter);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		leaveEditModeInternal();
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		leaveEditModeInternal();
	}

	private TextArea createPropertyTextArea(String id) {
		final TextArea textArea = new TextArea();
		textArea.setId(id);
		textArea.setWidthFull();
		textArea.setEnabled(false);
		return textArea;
	}

	private TextField createPropertyTextField(String id) {
		final TextField propertyField = new TextField();
		propertyField.setId(id);
		propertyField.setWidthFull();
		propertyField.addValueChangeListener(event -> {
			if (!event.isFromClient()) {
				return;
			}

			if (escapePressed) {
				escapePressed = false;
			} else if (binder.hasChanges()) {
				log.debug("value changed on %s from %s to %s".formatted(event.getSource().getId(), event.getOldValue(),
						event.getValue()));
				binder.writeBeanIfValid(entity);
				listener.entityChanged(entity);
			}
		});
		propertyField.addKeyDownListener(Key.ESCAPE, event -> {
			log.debug("ESC pressed, resetting field to old value");
			// propertyField.clear();
			escapePressed = true;
			// binder.readBean(entity);
			binder.refreshFields();
		});
		propertyField.setEnabled(false);
		return propertyField;
	}

	public void editEntity(T newEntity) {
		Assert.notNull(newEntity, "Empty entity not editable");
		setEntityInternal(newEntity);
		enterEditModeInternal();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void enterEditMode() {
	}

	private void enterEditModeInternal() {
//		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
//		enterEditMode();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void leaveEditMode() {
	}

	private void leaveEditModeInternal() {
//		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
//		editButton.setEnabled(entity != null);

		leaveEditMode();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void preSave() {
	}

	private void saveInternal(EntityDetailsListener<T> callback) {

		preSave();

		binder.writeBeanIfValid(entity);
//		callback.saveEntity(entity);
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 *
	 * @param entity the entity to set, or <code>null</code> to clear the fields
	 */
	protected void setEntity(T entity) {
	}

	private void setEntityInternal(T entity) {
		this.entity = entity;

		if (entity == null) {
			disableEditableFields();
		} else {
			enableEditableFields();
			processEntity(entity);
		}

		binder.readBean(entity);
//		editButton.setEnabled(entity != null);

		setEntity(entity);
	}

	private void enableEditableFields() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
	}

	private void disableEditableFields() {
		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
	}

	/**
	 * Override in subclasses to process entity, e.g. to calculate non persistent
	 * fields.
	 *
	 * @param entity the entity to be processed, not <code>null</code>
	 */
	protected void processEntity(T entity) {
	}

	public void showEntity(T entity) {
		setEntityInternal(entity);
		leaveEditModeInternal();
	}

}
