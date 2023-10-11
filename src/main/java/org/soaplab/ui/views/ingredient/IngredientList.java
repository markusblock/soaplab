package org.soaplab.ui.views.ingredient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.soaplab.domain.*;
import org.soaplab.service.ingredients.IngredientsService;
import org.soaplab.ui.views.StringToPriceValueConverter;

public class IngredientList extends Div {

    private static final long serialVersionUID = 1L;

    private final VerticalLayout content;

    @Getter(value = AccessLevel.PROTECTED)
    private final Grid<Ingredient> entityGrid;

    private final TextField searchField;

    private final IngredientsService ingredientsService;

    public IngredientList(IngredientsService ingredientsService) {

        this.ingredientsService = ingredientsService;

        content = new VerticalLayout();
        content.setSizeFull();
        add(content);

        final HorizontalLayout toolPanel = new HorizontalLayout();
        toolPanel.setWidthFull();
        content.add(toolPanel);

        searchField = new TextField();
        searchField.setId("entitylist.search");
        searchField.setWidthFull();
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterEntityList(event.getValue()));
        toolPanel.add(searchField);

        entityGrid = new Grid<Ingredient>();
        entityGrid.setId("entitylist.grid");
        entityGrid.setSelectionMode(SelectionMode.SINGLE);
        entityGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        entityGrid.setSelectionMode(SelectionMode.SINGLE);
        entityGrid.addColumn(ingredient -> getTranslationForIngredientType(ingredient))
                .setHeader(getTranslation("domain.entity.type"))
                .setSortable(true);
        entityGrid.addColumn(Ingredient::getName).setHeader(getTranslation("domain.entity.name")).setSortable(true);
        entityGrid.addColumn(Ingredient::getInci).setHeader(getTranslation("domain.ingredient.inci")).setSortable(true);
        entityGrid.addColumn(LitRenderer.<Ingredient> of("<b>${item.cost} €</b>")
                .withProperty("cost",
                        ingredient -> new StringToPriceValueConverter().convertToPresentation(ingredient.getCost(),
                                new ValueContext()))).setHeader(getTranslation("domain.ingredient.price"));
        content.add(entityGrid);

        setItems();
    }

    private String getTranslationForIngredientType(Ingredient ingredient) {
        if (ingredient instanceof Fat) {
            return getTranslation("domain.fat");
        } else if (ingredient instanceof Acid) {
            return getTranslation("domain.acid");
        } else if (ingredient instanceof Liquid) {
            return getTranslation("domain.liquid");
        } else if (ingredient instanceof Fragrance) {
            return getTranslation("domain.fragrance");
        } else if (ingredient instanceof KOH) {
            return getTranslation("domain.koh");
        } else if (ingredient instanceof NaOH) {
            return getTranslation("domain.naoh");
        } else if (ingredient instanceof Additive) {
            return getTranslation("domain.additive");
        } else {
            throw new IllegalArgumentException(
                    "Ingredient type %s not expected here.".formatted(ingredient.getClass()));
        }

    }

    private void filterEntityList(String searchString) {
        if (StringUtils.isEmpty(searchString)) {
            entityGrid.setItems(ingredientsService.findAll());
        } else {
            entityGrid.setItems(ingredientsService.findByNameOrInci(searchString));
        }
    }

    private void setItems() {
        filterEntityList(null);
    }

    public void select(Ingredient selectEntity) {
        if (selectEntity == null) {
            return;
        }
        entityGrid.select(selectEntity);
    }

    public void deselectAll() {
        entityGrid.deselectAll();
    }

    public void refreshAll() {
        setItems();
    }

    public void selectFirstEntity() {
        final List<Ingredient> entities = entityGrid.getListDataView().getItems().collect(Collectors.toList());
        if (entities.size() > 0) {
            select(entities.get(0));
        } else {
            select(null);
        }
    }

    public Optional<Ingredient> getSelectedEntity() {
        final SingleSelect<Grid<Ingredient>, Ingredient> entitySelect = entityGrid.asSingleSelect();
        return entitySelect.getOptionalValue();
    }

}
