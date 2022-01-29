package org.soaplab.ui;

import org.soaplab.ui.views.MenuBar;
import org.soaplab.ui.views.acid.AcidsView;
import org.soaplab.ui.views.fat.FatsView;
import org.soaplab.ui.views.fragrance.FragranceView;
import org.soaplab.ui.views.liquid.LiquidsView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RoutePrefix;

import lombok.extern.slf4j.Slf4j;

@RoutePrefix("soaplab/ui")
@Route("") // registers on the prefix path
@RouteAlias(value = "", absolute = true) // registers on the root path
@Slf4j
public class MainAppLayout extends AppLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;

	public MainAppLayout() {
		log.info("Using locale " + UI.getCurrent().getLocale());

		DrawerToggle toggle = new DrawerToggle();

		H1 title = new H1("Soaplab");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		MenuBar menuBar = new MenuBar();
		menuBar.addMenuItem(VaadinIcon.DASHBOARD, getTranslation("domain.fats"), FatsView.class);
		menuBar.addMenuItem(VaadinIcon.CART, getTranslation("domain.acids"), AcidsView.class);
		menuBar.addMenuItem(VaadinIcon.USER_HEART, getTranslation("domain.liquids"), LiquidsView.class);
		menuBar.addMenuItem(VaadinIcon.ACADEMY_CAP, getTranslation("domain.fragrances"), FragranceView.class);

		addToDrawer(menuBar.getMenuItemComponents());
		addToNavbar(toggle, title);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// redirect to default page only when the main view is called
		if (MainAppLayout.class.equals(event.getNavigationTarget())) {
			event.forwardTo(FatsView.class);
		}
	}

}
