package org.soaplab;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.EnumUtils;

public class TestSystemPropertyHelper {

	public enum TestEnvironment {
		LOCAL, CONTAINER
	}

	public enum TestBrowser {
		FIREFOX, CHROME
	}

	public enum TestLocale {
		DE, EN
	}

	public static boolean isHeadless() {
		final String property = System.getProperty("headless", Boolean.FALSE.toString());
		return BooleanUtils.toBoolean(property);
	}

	public static TestBrowser getBrowser() {
		final String property = System.getProperty("testBrowser", TestBrowser.FIREFOX.name());
		return EnumUtils.getEnumIgnoreCase(TestBrowser.class, property);
	}

	public static TestEnvironment getTestEnvironment() {
		final String property = System.getProperty("testEnvironment", TestEnvironment.LOCAL.name());
		return EnumUtils.getEnumIgnoreCase(TestEnvironment.class, property);
	}

	public static TestLocale getTestLocale() {
		final String property = System.getProperty("testLocale", TestLocale.EN.name());
		return EnumUtils.getEnumIgnoreCase(TestLocale.class, property);
	}

}
