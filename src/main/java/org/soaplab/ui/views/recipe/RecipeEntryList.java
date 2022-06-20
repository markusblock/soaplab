package org.soaplab.ui.views.recipe;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;

import org.soaplab.domain.Ingredient;
import org.soaplab.domain.Percentage;
import org.soaplab.domain.RecipeEntry;
import org.soaplab.repository.EntityRepository;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

public class RecipeEntryList<T extends Ingredient> extends Div {

	private static final long serialVersionUID = 1L;

	private VerticalLayout content;
	private Grid<RecipeEntry<T>> selectedEntities;

	private Editor<RecipeEntry<T>> editor;

	private Registration doubleClickListener;

	private Binder<RecipeEntry<T>> binder;

	@Getter
	private Collection<RecipeEntry<T>> data;

	public RecipeEntryList(EntityRepository<T> repository) {
		super();

		content = new VerticalLayout();
		content.setSizeFull();
		add(content);

		selectedEntities = new Grid<RecipeEntry<T>>();
		selectedEntities.setId("recipe.entitylist.grid");
		selectedEntities.setSelectionMode(SelectionMode.SINGLE);
		selectedEntities.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		selectedEntities.setSelectionMode(SelectionMode.SINGLE);
		Column<RecipeEntry<T>> fatNameColumn = selectedEntities.addColumn(entry -> entry.getIngredient().getName())
				.setHeader("Name").setSortable(true);
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		Column<RecipeEntry<T>> percentageColumn = selectedEntities
				.addColumn(new NumberRenderer<RecipeEntry<T>>(entry -> entry.getPercentage().getNumber(), formatter))
				.setHeader("%").setSortable(true);
		content.add(selectedEntities);

		binder = new Binder<>();
		editor = selectedEntities.getEditor();
		editor.setBinder(binder);

		ComboBox<T> fatSelector = new ComboBox<T>();
		fatSelector.setWidthFull();
		fatSelector.setItems(repository.findAll());
		fatSelector.setItemLabelGenerator(fat -> fat.getName());
		addCloseHandler(fatSelector, editor);
		binder.forField(fatSelector).asRequired("Fat must not be empty")
//        .withStatusLabel(firstNameValidationMessage)
				.bind(RecipeEntry<T>::getIngredient, RecipeEntry<T>::setIngredient);
		fatNameColumn.setEditorComponent(fatSelector);

		TextField percentageField = new TextField();
		percentageField.setWidthFull();
		addCloseHandler(percentageField, editor);
		binder.forField(percentageField)
		// .asRequired("First name must not be empty")
//		        .withStatusLabel(firstNameValidationMessage)
				.bind(entry -> entry.getPercentage().getNumber().toString(),
						(entry, fieldvalue) -> entry.setPercentage(new Percentage(new BigDecimal(fieldvalue))));
		percentageColumn.setEditorComponent(percentageField);

	}

	private void addCloseHandler(Component component, Editor<RecipeEntry<T>> editor) {
		component.getElement().addEventListener("keydown", e -> {
			editor.cancel();
			System.out.println("closing editor...");
			System.out.println("state of data " + data);
		}).setFilter("event.code === 'Escape'");
	}

	public void setData() {
		setData(null);
	}

	public void setData(Collection<RecipeEntry<T>> data) {
		this.data = data;
		if (data == null) {
			selectedEntities.setItems(Collections.emptyList());
		} else {
			selectedEntities.setItems(data);
		}
	}

	public void enterEditMode() {
		doubleClickListener = selectedEntities.addItemClickListener(e -> {
			editor.editItem(e.getItem());
			Component editorComponent = e.getColumn().getEditorComponent();
			if (editorComponent instanceof Focusable) {
				((Focusable<?>) editorComponent).focus();
			}
		});
	}

	public void leaveEditMode() {
		if (doubleClickListener != null) {
			doubleClickListener.remove();
		}

		if (editor != null && editor.isOpen()) {
			editor.closeEditor();
		}
	}
}
