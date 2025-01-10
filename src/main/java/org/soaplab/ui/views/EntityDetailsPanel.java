package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.soaplab.domain.Ingredient;
import org.soaplab.domain.NamedEntity;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.Price;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.domain.Weight;
import org.soaplab.repository.EntityRepository;
import org.springframework.util.Assert;

import com.vaadin.flow.component.BlurNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.InputNotifier;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import lombok.extern.slf4j.Slf4j;

//TODO
//table for fields (key - value+unit)
@Slf4j
public class EntityDetailsPanel<T extends NamedEntity> extends Div implements BeforeEnterObserver, BeforeLeaveObserver {

	private static final long serialVersionUID = 1L;
	public static final String PANEL_ID = "entitydetailspanel";

	private final VerticalLayout content;
	private final FormLayout commonEntityDetailsSection;
	private final FormLayout propertySection;

	private final Binder<T> binder;
	private final List<HasEnabled> editablePropertyFields;
	private final List<RecipeEntryTable<? extends Ingredient>> recipeEntryTables = new ArrayList<>();

	private Optional<T> entity;
	private boolean inEditMode;

	private final EntityDetailsListener<T> listener;

	protected EntityDetailsPanel(EntityDetailsListener<T> listener) {
		super();
		this.listener = listener;

		setId(PANEL_ID);

		editablePropertyFields = new ArrayList<>();

		binder = new Binder<>();

		content = new VerticalLayout();
		add(content);

		commonEntityDetailsSection = new FormLayout();
		commonEntityDetailsSection.setId(PANEL_ID + ".section.common");
		commonEntityDetailsSection.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1, LabelsPosition.ASIDE));
		content.add(commonEntityDetailsSection);

		final TextField idField = createPropertyTextField("domain.entity.id");
		commonEntityDetailsSection.addFormItem(idField, getTranslation("domain.entity.id"));
		binder.forField(idField).bindReadOnly(ingred -> Objects.toString(ingred.getId(), ""));

		addPropertyStringField("domain.entity.name", T::getName, T::setName, true);

		propertySection = new FormLayout();
		propertySection.setId(PANEL_ID + ".section.properties");
		propertySection.setResponsiveSteps(
				// Use two columns by default
				new ResponsiveStep("0", 3, LabelsPosition.TOP));
		content.add(propertySection);

		content.addDoubleClickListener(event -> {
			log.trace("received double click event");
			enterEditMode();
		});
	}

	protected <INGREDIENT extends Ingredient> void addRecipeEntryTable(RecipeEntryTable<INGREDIENT> recipeItemsTable) {
		content.add(recipeItemsTable);
		recipeEntryTables.add(recipeItemsTable);
		recipeItemsTable.setEntityTableListener(new DefaultEntityTableListener<RecipeEntry<INGREDIENT>>() {
			@Override
			public void entityChangedInEntityTable(RecipeEntry<INGREDIENT> entity) {
				log.trace("Entity changed subPanel %s".formatted(entity));
				updateEntityWithChangesFromUI();
				fireEntityChanged();
				leaveEditMode();
			}

			@Override
			public void entityTableEntersEditMode() {
				log.trace("subpanel entityTableEntersEditMode");
			}

			@Override
			public void entityTableLeavesEditMode() {
				log.trace("subpanel entityTableLeavesEditMode");
			}
		});
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
		leaveEditMode();
	}

	@Override
	public void beforeLeave(BeforeLeaveEvent event) {
		leaveEditMode();
	}

	private TextArea createPropertyTextArea(String id) {
		final TextArea propertyField = new TextArea();
		propertyField.setId(PANEL_ID + "." + id);
		propertyField.setWidthFull();
		propertyField.setEnabled(false);
		propertyField.setValueChangeMode(ValueChangeMode.EAGER);

		addValueChangedListener(propertyField);
		addKeyPressedListeners(propertyField);
		addFocusListener(propertyField);
		addInputListener(propertyField);
		addBlurListener(propertyField);

		return propertyField;
	}

	private TextField createPropertyTextField(String id) {
		final TextField propertyField = new TextField();
		propertyField.setId(PANEL_ID + "." + id);
		propertyField.setWidthFull();
		propertyField.setEnabled(false);
		// set to eager otherwise ESC won't reset the value in the current focussed
		// field
		propertyField.setValueChangeMode(ValueChangeMode.EAGER);

		addValueChangedListener(propertyField);
		addKeyPressedListeners(propertyField);
		addFocusListener(propertyField);
		addInputListener(propertyField);
		addBlurListener(propertyField);

		return propertyField;
	}

	private void addValueChangedListener(final TextFieldBase<?, String> propertyField) {
		propertyField.addValueChangeListener(event -> {
			if (!event.isFromClient()) {
				return;
			}

			final String oldValue = event.getOldValue();
			final String newValue = event.getValue();
			log.debug("received valueChangedEvent on %s from %s to %s".formatted(event.getSource().getId(), oldValue,
					newValue));
			if (!inEditMode) {
				log.trace("ignoring event because panel is not in edit mode");
			} else if (!Objects.equals(oldValue, newValue)) {
				log.trace("value changed from %s to %s".formatted(oldValue, newValue));
			}
		});
	}

	private void addInputListener(InputNotifier propertyField) {
		propertyField.addInputListener(event -> {
			if (!event.isFromClient()) {
				return;
			}
			log.debug("received input event on " + event.getSource().getId());
		});
	}

	private void addFocusListener(Focusable<?> propertyField) {
		propertyField.addFocusListener(event -> {
			if (!event.isFromClient()) {
				return;
			}
			log.debug("received focus event on " + event.getSource().getId());
			enterEditMode();
		});
	}

	private void addBlurListener(BlurNotifier<?> propertyField) {
		propertyField.addBlurListener(event -> {
			if (!event.isFromClient()) {
				return;
			}
			log.debug("received blur event on " + event.getSource().getId());
		});
	}

	private void addKeyPressedListeners(KeyNotifier propertyField) {
		propertyField.addKeyDownListener(Key.ESCAPE, event -> {
			log.debug("ESC pressed, resetting field to old value");
			setEntityInternal(entity);
		});
		propertyField.addKeyDownListener(Key.ENTER, event -> {
			log.debug("ENTER pressed");
			if (binder.hasChanges() || subPanelHasChanges()) {
				log.debug("binder or subPanel has changes");
				updateEntityWithChangesFromUI();
				fireEntityChanged();
			}
			leaveEditMode();
			// binder.refreshFields();
		});
	}

	protected boolean subPanelHasChanges() {
		return false;
	}

	public void editEntity(T newEntity) {
		log.trace("editEntity " + newEntity);
		Assert.notNull(newEntity, "Empty entity not editable");
		setEntityInternal(Optional.of(newEntity));
		enterEditMode();
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void enterEditMode() {
		if (!inEditMode && entity.isPresent()) {
			log.trace("enterEditMode");
			enableEditableFields();
			inEditMode = true;
			listener.entityDetailsPanelEntersEditMode();
		}
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 */
	protected void leaveEditMode() {
		if (inEditMode) {
			log.trace("leaveEditMode");
			inEditMode = false;
			disableEditableFields();
			listener.entityDetailsPanelLeavesEditMode();
		}
	}

	/**
	 * Override in subclasses to implement behaviour of special fields, e.g. update
	 * references of implicit entities
	 */
	protected void updateEntityWithChangesFromUI() {
		binder.writeBeanIfValid(entity.orElse(null));
	}

	protected void fireEntityChanged() {
		listener.entityChangedInEntityDetails(entity.orElse(null));
	}

	/**
	 * Override in subclasses to implement behaviour of special fields.
	 *
	 * @param entity the entity to set, or <code>null</code> to clear the fields
	 */
	protected void setEntity(Optional<T> entity) {
	}

	private void setEntityInternal(Optional<T> entity) {
		this.entity = entity;

		entity.ifPresent(t -> processEntity(t));

		binder.readBean(entity.orElse(null));
		setEntity(entity);

		leaveEditMode();
	}

	private void enableEditableFields() {
		log.trace("enableEditableFields");
		editablePropertyFields.forEach(tf -> tf.setEnabled(true));
		recipeEntryTables.forEach(table -> table.enterEditMode());
	}

	private void disableEditableFields() {
		log.trace("disableEditableFields");
		editablePropertyFields.forEach(tf -> tf.setEnabled(false));
		recipeEntryTables.forEach(table -> table.leaveEditMode());
	}

	/**
	 * Override in subclasses to process entity, e.g. to calculate non persistent
	 * fields.
	 *
	 * @param entity the entity to be processed, not <code>null</code>
	 */
	protected void processEntity(T entity) {
		log.trace("processEntity " + entity);
	}

	public void showEntity(Optional<T> entity) {
		log.trace("showEntity " + entity);
		setEntityInternal(entity);
		leaveEditMode();
	}

	public void cancelEditMode() {
		leaveEditMode();
	}
}
