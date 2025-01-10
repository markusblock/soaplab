package org.soaplab.ui.views;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.repository.EntityRepository;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import lombok.extern.slf4j.Slf4j;

@CssImport("./themes/soaplab/recipe_entry_list.css")
@Slf4j
public class RecipeEntryTable<T extends Ingredient> extends Div {

	private final class IngredientValueProvider implements ValueProvider<RecipeEntry<T>, String> {
		private static final long serialVersionUID = 1L;

		@Override
		public String apply(RecipeEntry<T> entry) {
			if (entry == null || entry.getIngredient() == null) {
				return "";
			}
			return entry.getIngredient().getName();
		}
	}

	private final class PercentageValueProvider implements ValueProvider<RecipeEntry<T>, Number> {
		private static final long serialVersionUID = 1L;

		@Override
		public Number apply(RecipeEntry<T> entry) {
			if (entry == null || entry.getPercentage() == null) {
				return null;
			}
			return entry.getPercentage().getNumber();
		}
	}

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;
	private EntityGrid<RecipeEntry<T>> grid;
	private Registration clickListener;
	private boolean editMode;
	private Button addButton;
	private Button removeButton;
	private boolean recipeEntryListChanged;

	public RecipeEntryTable(EntityRepository<T> repository, String headerTextId) {
		super();

		setSizeFull();
		setId("recipe.entitylist");

		content = new VerticalLayout();
		content.setSizeFull();
		content.setPadding(false);
		content.setSpacing(false);
		content.setMargin(false);
		add(content);

		grid = new EntityGrid<RecipeEntry<T>>();
		grid.setId("recipe.entitylist.grid");
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER);
		grid.getStyle().set("font-size", "var(--lumo-font-size-s)").set("margin", "0");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(
				event -> removeButton.setEnabled(editMode && event.getFirstSelectedItem().isPresent()));

		grid.setEnabled(false);
		content.add(grid);

		// NAME COLUMN
		final Column<RecipeEntry<T>> entityNameColumn = grid.addColumn(new IngredientValueProvider()).setSortable(true);
		// NAME EDITOR
		final ComboBox<T> entitySelector = new ComboBox<T>();
		entitySelector.setWidthFull();
		entitySelector.setItems(repository.findAll());
		entitySelector.setItemLabelGenerator(entity -> entity.getName());
		grid.getBinder().forField(entitySelector).asRequired("Entity must not be empty")
//        .withStatusLabel(firstNameValidationMessage)
				.bind(RecipeEntry<T>::getIngredient, RecipeEntry<T>::setIngredient);
		entityNameColumn.setEditorComponent(entitySelector);

		// PERCENTAGE COLUMN
		final NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		final Column<RecipeEntry<T>> percentageColumn = grid
				.addColumn(new NumberRenderer<RecipeEntry<T>>(new PercentageValueProvider(), formatter))
				.setSortable(true);
		// PERCENTAGE EDITOR
		final TextField percentageField = new TextField();
		percentageField.setWidthFull();
		grid.getBinder().forField(percentageField)
		// .asRequired("First name must not be empty")
//		        .withStatusLabel(firstNameValidationMessage)
				.bind(entry -> {
					if (entry == null || entry.getPercentage() == null) {
						return "";
					}
					return entry.getPercentage().getNumber().toString();
				}, (entry, fieldvalue) -> entry.setPercentage(new Percentage(new BigDecimal(fieldvalue))));
		percentageColumn.setEditorComponent(percentageField);

		final HorizontalLayout toolbar = createToolbar(headerTextId);
		toolbar.add(createAddButton());
		toolbar.add(createRemoveButton());
		final HeaderRow headerRow = grid.prependHeaderRow();
		// headerRow.join(entityNameColumn, percentageColumn);
		headerRow.getCell(entityNameColumn).setComponent(toolbar);

	}

	private HorizontalLayout createToolbar(String headerTextId) {
		final HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeFull();
		toolbar.setAlignItems(Alignment.CENTER);
		toolbar.setJustifyContentMode(JustifyContentMode.END);
		final H1 listHeader = new H1(getTranslation(headerTextId));
		listHeader.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		listHeader.setSizeFull();
		toolbar.add(listHeader);
		return toolbar;
	}

	private Button createRemoveButton() {
		removeButton = new Button();
		removeButton.setId("recipe.entitylist.removeRecipeEntry");
		removeButton.setIcon(VaadinIcon.MINUS.create());
		removeButton.addClickListener(e -> this.removeRecipeEntry());
		removeButton.setEnabled(false);
		removeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		return removeButton;
	}

	private Button createAddButton() {
		addButton = new Button();
		addButton.setId("recipe.entitylist.addRecipeEntry");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(e -> addRecipeEntry());
		addButton.setEnabled(false);
		addButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		return addButton;
	}

	private void addRecipeEntry() {
		recipeEntryListChanged = true;
		final RecipeEntry<T> recipeEntry = RecipeEntry.<T>builder().id(UUID.randomUUID()).build();
		final List<RecipeEntry<T>> newData = new ArrayList<>(grid.getEntities());
		newData.add(recipeEntry);
		grid.setEntities(newData);
		grid.openEditorOnEntity(recipeEntry);
	}

	private void removeRecipeEntry() {
		recipeEntryListChanged = true;
		grid.getSelectedEntity().ifPresent(selectedEntity -> grid.removeEntity(selectedEntity));
	}

	public void setEntities() {
//		setData(null);
		setEntities(null);
	}

	public void setEntities(List<RecipeEntry<T>> data) {
//		this.data = data;
		if (data == null) {
			grid.setEntities(Collections.emptyList());
//			grid.setItems(Collections.emptyList());
			grid.setHeight("50px");
		} else {
			grid.setEntities(data);
//			grid.setItems(data);
			grid.setHeightFull();
			if (data.size() < 10) {
				grid.setAllRowsVisible(true);
			}
		}
	}

	public void enterEditMode() {
		if (!editMode) {
			log.trace("%s: enterEditMode".formatted(getId()));
			clickListener = grid.addItemClickListener(e -> {
				// also called when item gets removed = click in the row
				if (e.getItem() == null) {
					return;
				}
				grid.getEditor().editItem(e.getItem());
				final Component editorComponent = e.getColumn().getEditorComponent();
				if (editorComponent instanceof Focusable) {
					((Focusable<?>) editorComponent).focus();
				}
			});
			this.editMode = true;
			setTableComponentsState(editMode);
		}
	}

	public void leaveEditMode() {
		if (editMode) {
			log.trace("%s: leaveEditMode".formatted(getId()));
			grid.cancelEditMode();

			if (clickListener != null) {
				clickListener.remove();
			}

			this.editMode = false;
			setTableComponentsState(editMode);
		}
	}

	private void setTableComponentsState(boolean editMode) {
		grid.setEnabled(editMode);
		addButton.setEnabled(editMode);
		removeButton.setEnabled(editMode);
	}

	public boolean hasChanges() {
		return recipeEntryListChanged;
	}

	public List<RecipeEntry<T>> getEntities() {
		return grid.getEntities();
	}

	public void setEntityTableListener(EntityTableListener<RecipeEntry<T>> entityTableListener) {
		grid.setEntityTableListener(entityTableListener);
	}
}
