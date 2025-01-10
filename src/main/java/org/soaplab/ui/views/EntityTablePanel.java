package org.soaplab.ui.views;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.soaplab.domain.Entity;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
public class EntityTablePanel<T extends Entity> extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	public static final String PANEL_ID = "entitytablepanel";

	private final EntityGrid<T> grid;
	private final HeaderRow searchHeaderRow;

	public EntityTablePanel(Class<T> entityClass, EntityTableListener<T> tableListener) {
		super();

		setSizeFull();
		setId(PANEL_ID);

		grid = new EntityGrid<>(entityClass, tableListener);
		grid.getHeaderRows().clear();
		add(grid);

		searchHeaderRow = grid.appendHeaderRow();
	}

	public void cancelEditMode() {
		grid.cancelEditMode();
	}

	public void clearSelection() {
		grid.clearSelection();

	}

	public Optional<T> getSelectedEntity() {
		return grid.getSelectedEntity();
	}

	public void setEntities(List<T> entities) {
		grid.setEntities(entities);
	}

	public void selectFirstEntity() {
		grid.selectFirstEntity();
	}

	public void selectEntity(T selectEntity) {
		grid.selectEntity(selectEntity);
	}

	public void refreshEntity(T entity) {
		grid.refreshEntity(entity);
	}

	protected Column<T> addEntityNameColumn(String propertyName, String id) {
		return addEntityNameColumn(propertyName, id);
	}

	protected Column<T> addIntegerColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, new StringToIntegerConverter(""));
	}

	protected Column<T> addBigDecimalColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, new MyStringToBigDecConverter());
	}

	protected Column<T> addPercentageColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, new StringToPercentageConverter(), new Div(new Text("%")));
	}

	protected Column<T> addPriceColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, new StringToPriceValueConverter(), new Div(new Text("€")));
	}

	protected Column<T> addEntityColumn(String propertyName, String id) {
		return addEntityColumn(propertyName, id, null, null);
	}

	protected <PROPERTY_TYPE> Column<T> addEntityColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter) {
		return addEntityColumn(propertyName, id, converter, null);
	}

	protected <PROPERTY_TYPE> Column<T> addEntityColumn(String propertyName, String id,
			Converter<String, PROPERTY_TYPE> converter, Component suffixComponent) {
		final EntityFilter<T> entityFilter = grid.getEntityFilter();
		final Grid.Column<T> column = grid.addEntityPropertyColumn(propertyName, id, converter, suffixComponent);
		searchHeaderRow.getCell(column)
				.setComponent(createFilterHeader(propertyName, id, entityFilter::setFilterToProperty));
		return column;
	}

	private Component createFilterHeader(String propertyName, String id,
			BiConsumer<String, String> filterChangeConsumer) {
		final String headerValue = getTranslation(id);
		final NativeLabel label = new NativeLabel(headerValue);
		label.getStyle().set("padding-top", "var(--lumo-space-m)").set("font-size", "var(--lumo-font-size-xs)");

		final TextField textField = new TextField();
		textField.setId(grid.getId().orElseThrow() + "." + id + ".filter");
		textField.setValueChangeMode(ValueChangeMode.EAGER);
		textField.setClearButtonVisible(true);
		textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		textField.addValueChangeListener(e -> filterChangeConsumer.accept(propertyName, e.getValue()));

		final VerticalLayout layout = new VerticalLayout(label, textField);
		layout.getThemeList().clear();
		layout.getThemeList().add("spacing-xs");

		return layout;
	}

}
