package org.soaplab.ui;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.soaplab.ui.i18n.TranslationProvider;
import org.soaplab.ui.views.MenuBar;
import org.soaplab.ui.views.acid.AcidsView;
import org.soaplab.ui.views.fat.FatsView;
import org.soaplab.ui.views.fragrance.FragranceView;
import org.soaplab.ui.views.ingredient.IngredientView;
import org.soaplab.ui.views.koh.KOHView;
import org.soaplab.ui.views.liquid.LiquidsView;
import org.soaplab.ui.views.lyerecipe.LyeRecipeView;
import org.soaplab.ui.views.naoh.NaOHView;
import org.soaplab.ui.views.recipe.RecipeView;
import org.springframework.beans.factory.annotation.Autowired;

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

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@RoutePrefix("soaplab/ui")
@Route("") // registers on the route prefix path
@RouteAlias(value = "", absolute = true) // registers on the root path
@Slf4j
public class MainAppLayout extends AppLayout implements BeforeEnterObserver {

	private static final String LOCALE = "locale";
	private static final long serialVersionUID = 1L;

	@Autowired
	public MainAppLayout(TranslationProvider translationProvider) {

		final DrawerToggle toggle = new DrawerToggle();
		toggle.setId("soaplab.id");

		final H1 title = new H1("Soaplab");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		final Optional<Locale> cookieLocale = findLocaleFromCookie();
		cookieLocale.ifPresentOrElse(locale -> log.info("Found language in cookie: {}", locale),
				() -> log.info("Found NO language in cookie"));
		final Select<Locale> languageSelect = new Select<>();
		languageSelect.setId("soaplab.languageselect.id");
		languageSelect.setMaxWidth(70, Unit.POINTS);
		languageSelect.setItems(translationProvider.getProvidedLocales());
		languageSelect.setItemLabelGenerator(l -> getTranslation("i18n." + l.getLanguage()));
		final Locale defaultLocale = UI.getCurrent().getLocale();
		Locale localeToSelect = defaultLocale;
		if (cookieLocale.isPresent()) {
			localeToSelect = translationProvider.getProvidedLocales().stream().filter(l -> l.equals(cookieLocale.get()))
					.findFirst().orElse(defaultLocale);
		} else {
			saveLocaleToCookie(defaultLocale);
		}

		setLocale(localeToSelect);
		languageSelect.setValue(localeToSelect);
		languageSelect.addValueChangeListener(event -> {
			saveLocaleToCookie(event.getValue());
			setLocale(event.getValue());
		});

		final MenuBar menuBar = new MenuBar();
		menuBar.addMenuItem(VaadinIcon.DASHBOARD, getTranslation("domain.fats"), FatsView.class);
		menuBar.addMenuItem(VaadinIcon.CART, getTranslation("domain.acids"), AcidsView.class);
		menuBar.addMenuItem(VaadinIcon.USER_HEART, getTranslation("domain.liquids"), LiquidsView.class);
		menuBar.addMenuItem(VaadinIcon.ACADEMY_CAP, getTranslation("domain.fragrances"), FragranceView.class);
		menuBar.addMenuItem(VaadinIcon.ALARM, getTranslation("domain.naoh"), NaOHView.class);
		menuBar.addMenuItem(VaadinIcon.AMBULANCE, getTranslation("domain.koh"), KOHView.class);
		menuBar.addMenuItem(VaadinIcon.ABACUS, getTranslation("domain.recipes"), RecipeView.class);
		menuBar.addMenuItem(VaadinIcon.ADOBE_FLASH, getTranslation("domain.lyerecipes"), LyeRecipeView.class);
		menuBar.addMenuItem(VaadinIcon.DOLLAR, getTranslation("domain.ingredients"), IngredientView.class);

		addToDrawer(menuBar.getMenuItemComponents());
		final Label spacing = new Label();
		spacing.setSizeFull();
		addToNavbar(toggle, title, spacing, languageSelect);
	}

	/**
	 * Stores the users locale in a 'locale' cookie.
	 */
	private void saveLocaleToCookie(Locale locale) {
		log.info("Saving locale {} in cookie", locale);
		final Cookie cookie = new Cookie(LOCALE, locale.toLanguageTag());
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		// TODO migrate to Springboot 3
		VaadinService.getCurrentResponse().addCookie(cookie);
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
	private Optional<Locale> findLocaleFromCookie() {
		// TODO migrate to Springboot 3
		final Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
		if (cookies == null) {
			return Optional.empty();
		}
		for (final Cookie cookie : Arrays.asList(cookies)) {
			if (LOCALE.equals(cookie.getName())) {
				final String value = cookie.getValue();
				if (StringUtils.isEmpty(value)) {
					return Optional.empty();
				}
				return Optional.of(Locale.forLanguageTag(value));
			}
		}
		return Optional.empty();

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// redirect to default page only when the main view is called
		if (MainAppLayout.class.equals(event.getNavigationTarget())) {
			event.forwardTo(FatsView.class);
		}
	}
}
