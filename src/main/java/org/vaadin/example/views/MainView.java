package org.vaadin.example.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.service.GreetService;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

/**
 * A sample Vaadin view class.
 * 
Purpose: Views in a Vaadin application represent 
the UI components and layouts that are rendered in the web browser. 
They are responsible for displaying data to the user and handling user interactions on the client side.

Functionality: Views are defined using Vaadin components and layouts. 
They contain the visual elements of the application, such as forms, buttons, 
tables, etc. Views can interact with backend services directly or through 
controllers to fetch and display data.
 */
@Route
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed
     *            bean.
     */
    public MainView(@Autowired GreetService service) {

        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addClassName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello", e -> {
            add(new Paragraph(service.greet(textField.getValue())));
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in
        // styles.css.
        addClassName("centered-content");

        add(textField, button);
    }

}
