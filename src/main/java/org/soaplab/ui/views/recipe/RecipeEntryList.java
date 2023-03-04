package org.soaplab.ui.views.recipe;

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
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

@CssImport("./themes/soaplab/recipe_entry_list.css")
public class RecipeEntryList<T extends Ingredient> extends Div {

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
	private Grid<RecipeEntry<T>> selectedEntities;

	private Registration doubleClickListener;

	private Binder<RecipeEntry<T>> binder;

	@Getter
	private List<RecipeEntry<T>> data;

	private Column<RecipeEntry<T>> manageColumn;

	private boolean editMode;

	private Button addButton;

	public RecipeEntryList(EntityRepository<T> repository, String headerTextId) {
		super();

		content = new VerticalLayout();
		content.setSizeFull();
		content.setPadding(false);
		content.setSpacing(false);
		content.setMargin(false);
		add(content);

		selectedEntities = new Grid<RecipeEntry<T>>();
		selectedEntities.setId("recipe.entitylist.grid");
		selectedEntities.setSelectionMode(SelectionMode.SINGLE);
		selectedEntities.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT,
				GridVariant.LUMO_NO_BORDER);
		selectedEntities.setSelectionMode(SelectionMode.SINGLE);
		Column<RecipeEntry<T>> fatNameColumn = selectedEntities.addColumn(new IngredientValueProvider())
				.setHeader(getTranslation("domain.entity.name")).setSortable(true);
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		Column<RecipeEntry<T>> percentageColumn = selectedEntities
				.addColumn(new NumberRenderer<RecipeEntry<T>>(new PercentageValueProvider(), formatter)).setHeader("%")
				.setSortable(true);
		manageColumn = selectedEntities.addComponentColumn(recipeEntry -> {
			Button removeButton = new Button();
			removeButton.setId("recipe.entitylist.remove");
			removeButton.addClickListener(e -> this.removeRecipeEntry(recipeEntry));
			removeButton.setIcon(VaadinIcon.MINUS.create());
			removeButton.setEnabled(editMode);
//			removeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
//			removeButton.setWidth("24px");
//			removeButton.setHeight("24px");
			return removeButton;
//			HorizontalLayout toolbar = new HorizontalLayout();
//			toolbar.setSizeFull();
//			toolbar.add(removeButton);
//			toolbar.setJustifyContentMode(JustifyContentMode.END);
//			toolbar.setPadding(false);
//			toolbar.setSpacing(false);
//			toolbar.setMargin(false);
//			toolbar.setHeight("24px");
//			toolbar.setAlignItems(Alignment.CENTER);
//			return toolbar;
		}).setHeader(getTranslation("recipe.entitylist.grid.header.manage")).setFlexGrow(0).setAutoWidth(true)
				.setTextAlign(ColumnTextAlign.END);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeFull();
		toolbar.setAlignItems(Alignment.CENTER);
		toolbar.setJustifyContentMode(JustifyContentMode.END);
		H1 listHeader = new H1(getTranslation(headerTextId));
		listHeader.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		listHeader.setSizeFull();
		toolbar.add(listHeader);
		addButton = new Button();
		addButton.setId("recipe.entitylist.addEntity");
		addButton.setIcon(VaadinIcon.PLUS.create());
		addButton.addClickListener(e -> addRecipeEntry());
		addButton.setEnabled(false);
		addButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		toolbar.add(addButton);

		HeaderRow headerRow = selectedEntities.prependHeaderRow();
		headerRow.join(fatNameColumn, percentageColumn, manageColumn);
		headerRow.getCell(fatNameColumn).setComponent(toolbar);

		content.add(selectedEntities);

		binder = new Binder<>();
		selectedEntities.getEditor().setBinder(binder);

		ComboBox<T> fatSelector = new ComboBox<T>();
		fatSelector.setWidthFull();
		fatSelector.setItems(repository.findAll());
		fatSelector.setItemLabelGenerator(fat -> fat.getName());
		addCloseHandler(fatSelector);
		binder.forField(fatSelector).asRequired("Fat must not be empty")
//        .withStatusLabel(firstNameValidationMessage)
				.bind(RecipeEntry<T>::getIngredient, RecipeEntry<T>::setIngredient);
		fatNameColumn.setEditorComponent(fatSelector);

		TextField percentageField = new TextField();
		percentageField.setWidthFull();
		addCloseHandler(percentageField);
		binder.forField(percentageField)
		// .asRequired("First name must not be empty")
//		        .withStatusLabel(firstNameValidationMessage)
				.bind(entry -> {
					if (entry == null || entry.getPercentage() == null) {
						return "";
					}
					return entry.getPercentage().getNumber().toString();
				}, (entry, fieldvalue) -> entry.setPercentage(new Percentage(new BigDecimal(fieldvalue))));
		percentageColumn.setEditorComponent(percentageField);

	}

	private void addRecipeEntry() {
		RecipeEntry<T> recipeEntry = RecipeEntry.<T>builder().id(UUID.randomUUID()).build();
		List<RecipeEntry<T>> newData = new ArrayList<>(this.data);
		newData.add(recipeEntry);
		setData(newData);
	}

	private void removeRecipeEntry(RecipeEntry<T> recipeEntry) {
		List<RecipeEntry<T>> newData = new ArrayList<>(this.data);
		newData.remove(recipeEntry);
		setData(newData);
	}

	private void addCloseHandler(Component component) {
		component.getElement().addEventListener("keydown", e -> {
			selectedEntities.getEditor().cancel();
			System.out.println("closing editor...");
			System.out.println("state of data " + data);
			// Note! some browsers return key as Escape and some as Esc
		}).setFilter("event.key === 'Escape' || event.key === 'Esc'");
	}

	public void setData() {
		setData(null);
	}

	public void setData(List<RecipeEntry<T>> data) {
		this.data = data;
		if (data == null) {
			selectedEntities.setItems(Collections.emptyList());
			selectedEntities.setHeight("50px");
		} else {
			selectedEntities.setItems(data);
			selectedEntities.setHeightFull();
			if (data.size() < 10) {
				selectedEntities.setAllRowsVisible(true);
			}
		}
	}

	public void enterEditMode() {
		doubleClickListener = selectedEntities.addItemClickListener(e -> {
			// also called when item gets removed = click in the row
			if (e.getItem() == null) {
				return;
			}
			selectedEntities.getEditor().editItem(e.getItem());
			Component editorComponent = e.getColumn().getEditorComponent();
			if (editorComponent instanceof Focusable) {
				((Focusable<?>) editorComponent).focus();
			}
		});
		this.editMode = true;
		addButton.setEnabled(editMode);
	}

	public void leaveEditMode() {
		if (doubleClickListener != null) {
			doubleClickListener.remove();
		}

		if (selectedEntities.getEditor() != null && selectedEntities.getEditor().isOpen()) {
			selectedEntities.getEditor().closeEditor();
		}
		this.editMode = false;
		addButton.setEnabled(editMode);
	}
}
