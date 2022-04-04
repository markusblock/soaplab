package org.soaplab.ui.i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TranslationProvider implements I18NProvider {

	private static final long serialVersionUID = 1L;

	public static final String RESOURCE_BUNDLE_NAME = "translation";

	public static final Locale LOCALE_EN = new Locale("en", "GB");
	public static final Locale LOCALE_DE = new Locale("de", "DE");

	private static final ResourceBundle RESOURCE_BUNDLE_EN = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, LOCALE_EN);
	// TODO implement
	// private static final ResourceBundle RESOURCE_BUNDLE_DE =
	// ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, LOCALE_DE);

	private List<Locale> locales = Collections.unmodifiableList(Arrays.asList(LOCALE_EN, LOCALE_DE));

	@Override
	public List<Locale> getProvidedLocales() {
		return locales;
	}

	public String getTranslationPlural(String key, Object... params) {
		String pluralKeySuffix = "_plural";
		return getTranslation(key + pluralKeySuffix, params);
	}

	public String getTranslation(String key, Object... params) {
		return getTranslation(key, null, params);
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		if (key == null) {
			log.warn("Got lang request for key with null value!");
			return "";
		}

		ResourceBundle resourceBundle = RESOURCE_BUNDLE_EN;

		String value;
		try {
			value = resourceBundle.getString(key);
		} catch (final MissingResourceException e) {
			log.warn("Missing resource", e);
			return "!" + locale.getLanguage() + ": " + key;
		}
		if (params.length > 0) {
			value = MessageFormat.format(value, params);
		}
		return value;
	}
}
