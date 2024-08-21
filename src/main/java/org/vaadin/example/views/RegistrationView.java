package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.service.UserService;

@Route("register")
public class RegistrationView extends VerticalLayout {

    private final UserService userService;

    public RegistrationView(UserService userService) {
        this.userService = userService;

        addClassName("register-view");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create overlay box
        Div overlayBox = new Div();
        overlayBox.addClassName("overlay-box");

        // Create a logo header
        H2 logo = new H2("Register for FinTrack");
        logo.addClassName("logo");

        TextField username = new TextField("Please enter your desired Username");
        PasswordField password = new PasswordField("Please enter your desired Password");

        Button registerButton = new Button("Register", event -> {
            String name = username.getValue().trim();
            String pass = password.getValue().trim();

            if (name.isEmpty() || pass.isEmpty()) {
                Notification.show("Please fill in all fields", 3000, Notification.Position.TOP_CENTER);
            } else if (userService.findUserByName(name) != null) {
                Notification.show("Username already taken", 3000, Notification.Position.TOP_CENTER);
            } else {
                userService.registerUser(name, pass);
                Notification.show("Registration successful", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate(""));
            }
        });

        Button loginButton = new Button("Already have an account? Login here", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        VerticalLayout formLayout = new VerticalLayout(username, password, registerButton, loginButton);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.STRETCH);

        // Add header and form to overlay box
        overlayBox.add(logo, formLayout);

        add(overlayBox);
    }
}
