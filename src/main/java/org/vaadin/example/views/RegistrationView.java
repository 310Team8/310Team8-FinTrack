package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.service.UserService;

@Route("register")
public class RegistrationView extends VerticalLayout {

    @Autowired
    private UserService userService;

    public RegistrationView() {
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");

        Button registerButton = new Button("Register", event -> {
            String name = username.getValue();
            String pass = password.getValue();

            if (userService.findUserByName(name) != null) {
                Notification.show("Username already taken", 3000, Notification.Position.TOP_CENTER);
            } else {
                userService.registerUser(name, pass);
                Notification.show("Registration successful", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
        });

        Button loginButton = new Button("Already have an account? Login here", event -> {
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(username, password, registerButton, loginButton);
    }
}
