package org.soaplab.ui;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.soaplab.ui.i18n.TranslationProvider;
import org.soaplab.ui.views.MenuBar;
import org.soaplab.ui.views.acid.AcidsView;
import org.soaplab.ui.views.fat.FatsView;
import org.soaplab.ui.views.fragrance.FragranceView;
import org.soaplab.ui.views.liquid.LiquidsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import lombok.extern.slf4j.Slf4j;

@RoutePrefix("soaplab/ui")
@Route("") // registers on the prefix path
@RouteAlias(value = "", absolute = true) // registers on the root path
@Slf4j
public class MainAppLayout extends AppLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private TranslationProvider translationProvider;

	@Autowired
	public MainAppLayout(TranslationProvider translationProvider) {
		this.translationProvider = translationProvider;

		DrawerToggle toggle = new DrawerToggle();
		toggle.setId("soaplab.id");

		H1 title = new H1("Soaplab");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		String cookieLang = findLocaleFromCookie();
		Select<Locale> languageSelect = new Select<>();
		languageSelect.setMaxWidth(70, Unit.POINTS);
		languageSelect.setItems(translationProvider.getProvidedLocales());
		languageSelect.setItemLabelGenerator(l -> getTranslation("i18n." + l.getLanguage()));
		Locale localeToSelect;
		Locale defaultLocale = UI.getCurrent().getLocale();
		if (ObjectUtils.isEmpty(languageSelect)) {
			localeToSelect = defaultLocale;
			saveLocaleToCookie(localeToSelect);
		} else {
			localeToSelect = translationProvider.getProvidedLocales().stream()
					.filter(l -> l.equals(Locale.forLanguageTag(cookieLang))).findFirst().orElse(defaultLocale);
		}
		setLocale(localeToSelect);
		languageSelect.setValue(localeToSelect);
		languageSelect.addValueChangeListener(event -> {
			saveLocaleToCookie(event.getValue());
			setLocale(event.getValue());
		});
		log.info("Using locale " + localeToSelect);

		MenuBar menuBar = new MenuBar();
		menuBar.addMenuItem(VaadinIcon.DASHBOARD, getTranslation("domain.fats"), FatsView.class);
		menuBar.addMenuItem(VaadinIcon.CART, getTranslation("domain.acids"), AcidsView.class);
		menuBar.addMenuItem(VaadinIcon.USER_HEART, getTranslation("domain.liquids"), LiquidsView.class);
		menuBar.addMenuItem(VaadinIcon.ACADEMY_CAP, getTranslation("domain.fragrances"), FragranceView.class);

		addToDrawer(menuBar.getMenuItemComponents());
		Label spacing = new Label();
		spacing.setSizeFull();
		addToNavbar(toggle, title, spacing, languageSelect);
	}

	/**
	 * Stores the users locale in a 'locale' cookie.
	 */
	private void saveLocaleToCookie(Locale locale) {
		log.info("Saving locale {} in cookie", locale);
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
		Notification.show(getTranslation("menu.locale.saved", locale.getLanguage()));
	}

	/**
	 * Set locale to {@link VaadinSession} and {@link UI}.
	 */
	private void setLocale(Locale locale) {
		log.info("Setting locale to {}", locale);
		UI.getCurrent().setLocale(locale);
		VaadinSession.getCurrent().setLocale(locale);
	}

	/**
	 * Searches for a locale cookie and return it if it was found. Empty string
	 * otherwise.
	 */
	private String findLocaleFromCookie() {
		final Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
		if (cookies == null) {
			return "";
		}
		final Optional<String> cookie = Arrays.asList(cookies).stream().filter(c -> "locale".equals(c.getName()))
				.map(c -> c.getValue()).findAny();
		String foundLanguageInCookie = cookie.orElse("");
		log.info("Found language in cookie {}", foundLanguageInCookie);
		return foundLanguageInCookie;
	}

	private void clearLocalePreference() {
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", null));
		getUI().get().getPage().reload();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// redirect to default page only when the main view is called
		if (MainAppLayout.class.equals(event.getNavigationTarget())) {
			event.forwardTo(FatsView.class);
		}
	}

}
