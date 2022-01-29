package org.soaplab.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MenuBar extends Div {

	private static final long serialVersionUID = 1L;

	private Tabs menuComponentItems;

	public MenuBar() {
		menuComponentItems = new Tabs();
		menuComponentItems.setOrientation(Tabs.Orientation.VERTICAL);
	}

	public void addMenuItem(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
		Icon icon = viewIcon.create();
		icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
				.set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

		RouterLink link = new RouterLink();
		link.add(icon, new Span(viewName));
		link.setRoute(viewClass);
		link.setTabIndex(-1);

		menuComponentItems.add(new Tab(link));
	}

	public Component getMenuItemComponents() {
		return menuComponentItems;
	}

}
