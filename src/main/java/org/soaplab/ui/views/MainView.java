package org.soaplab.ui.views;

import org.soaplab.domain.Fat;
import org.soaplab.repository.FatRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	public MainView(FatRepository fatRepository) {
		add(new Text("Welcome to MainView."));
		add(new Text("3"));
		Grid<Fat> grid = new Grid<>();
		add(grid);
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addColumn(Fat::getName).setHeader("Name");
		grid.addColumn(Fat::getInci).setHeader("INCI");
		grid.addColumn(Fat::getIns).setHeader("INS");
		grid.addColumn(Fat::getSapNaoh).setHeader("Sap NaOH");
		grid.addColumn(Fat::getIodine).setHeader("Iodine");
		grid.addColumn(Fat::getLauric).setHeader("Lauric");
		grid.addColumn(Fat::getMyristic).setHeader("Myristic");
		grid.addColumn(Fat::getPalmitic).setHeader("Palmitic");
		grid.addColumn(Fat::getStearic).setHeader("Stearic");
		grid.addColumn(Fat::getRicinoleic).setHeader("Ricinoleic");
		grid.addColumn(Fat::getOleic).setHeader("Oleic");
		grid.addColumn(Fat::getLinoleic).setHeader("Linoleic");
		grid.addColumn(Fat::getLinolenic).setHeader("Linolenic");

		grid.setItems(fatRepository.findAll());
	}
}
