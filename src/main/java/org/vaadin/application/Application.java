package org.vaadin.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application. This class is responsible for
 * bootstrapping
 * the application and configuring it with Vaadin and Spring Boot integration.
 *
 * <p>
 * The {@code @SpringBootApplication} annotation marks this class as the main
 * configuration
 * class for the Spring Boot application, enabling component scanning and
 * auto-configuration.
 * </p>
 *
 * <p>
 * The {@code @PWA} annotation is used to make the application a Progressive Web
 * App (PWA),
 * allowing it to be installable on phones, tablets, and some desktop browsers.
 * This annotation
 * includes the application's name and a short name used in various contexts.
 * </p>
 *
 * <p>
 * The {@code @Theme} annotation specifies the theme to be used for styling the
 * Vaadin
 * components in the application.
 * </p>
 *
 * @see com.vaadin.flow.component.page.AppShellConfigurator
 * @see org.springframework.boot.SpringApplication
 */
@SpringBootApplication
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme("my-theme")
public class Application implements AppShellConfigurator {

    /**
     * The main method serves as the entry point of the Spring Boot application.
     * It delegates to Spring Boot's {@link SpringApplication#run} method to launch
     * the application.
     *
     * @param args command-line arguments passed to the application (if any)
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
