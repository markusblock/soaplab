package org.soaplab;

import org.soaplab.ui.i18n.TranslationProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
//@Theme(value = "soaplab", variant = Lumo.DARK)
//@PWA(name = "SoapLab", shortName = "SoapLab", offlineResources = { "images/logo.png" })
@Push
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class SoaplabApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		System.setProperty("vaadin.i18n.provider", TranslationProvider.class.getName());

		SpringApplication.run(SoaplabApplication.class, args);
	}
}
