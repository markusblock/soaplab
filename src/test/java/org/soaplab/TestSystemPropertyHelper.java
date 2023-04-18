package org.soaplab;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.EnumUtils;

public class TestSystemPropertyHelper {

	public enum TestEnvironment {
		//TODO adapt with CONTAINER
		LOCAL, CONTAINER
	}

	public enum TestBrowser {
		//TODO check if possible with testcontainers
		FIREFOX, CHROME
	}

	public enum TestLocale {
		DE, EN
	}

	public static boolean isHeadless() {
		String property = System.getProperty("headless", Boolean.FALSE.toString());
		return BooleanUtils.toBoolean(property);
	}

	public static TestBrowser getBrowser() {
		String property = System.getProperty("testBrowser", TestBrowser.FIREFOX.name());
		return EnumUtils.getEnumIgnoreCase(TestBrowser.class, property);
	}

	public static TestEnvironment getTestEnvironment() {
		String property = System.getProperty("testEnvironment", TestEnvironment.LOCAL.name());
		return EnumUtils.getEnumIgnoreCase(TestEnvironment.class, property);
	}

	public static TestLocale getTestLocale() {
		String property = System.getProperty("testLocale", TestLocale.EN.name());
		return EnumUtils.getEnumIgnoreCase(TestLocale.class, property);
	}

}
