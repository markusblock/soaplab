package org.soaplab.ui.views.development;

import org.soaplab.domain.utils.OliveOilSoapRecipeRepositoryTestData;
import org.soaplab.ui.MainAppLayout;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = "dev", layout = MainAppLayout.class)
public class DevelopmentView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final OliveOilSoapRecipeRepositoryTestData testData;

	private final H1 title;

	@Autowired
	public DevelopmentView(OliveOilSoapRecipeRepositoryTestData testData) {
		super();
		this.testData = testData;

		final HorizontalLayout headerPanel = new HorizontalLayout();
		headerPanel.setWidthFull();
		add(headerPanel);

		title = new H1("Development Utilities");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
		headerPanel.add(title);

		final HorizontalLayout contentPanel = new HorizontalLayout();
		contentPanel.setWidthFull();
		add(contentPanel);

		final Button createTestDataButton = new Button("Create TestData");
		createTestDataButton.addClickListener(event -> {
			log.trace("createTestDataButton clicked");
			this.testData.createSoapRecipe();
		});
		contentPanel.add(createTestDataButton);

		setSizeFull();
	}
}
