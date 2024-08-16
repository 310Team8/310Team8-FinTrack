package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.User;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

@Route("")
public class LoginView extends VerticalLayout {

    private final UserService userService;
    private final SessionService sessionService;

    public LoginView(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;

        setSizeFull(); 
        setAlignItems(Alignment.CENTER); 
        setJustifyContentMode(JustifyContentMode.CENTER); 

        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");

        Button loginButton = new Button("Login", event -> {
            String name = username.getValue();
            String pass = password.getValue();

            User user = userService.loginUser(name, pass);
            if (user != null) {
                sessionService.setLoggedInUserId(user.getId());
                Notification.show("Login successful", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } else {
                Notification.show("Invalid username or password", 3000, Notification.Position.TOP_CENTER);
            }
        });

        Button registerButton = new Button("Don't have an account? Register here", event -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        VerticalLayout formLayout = new VerticalLayout(username, password, loginButton, registerButton);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.STRETCH); 
        add(formLayout);
    }
}
