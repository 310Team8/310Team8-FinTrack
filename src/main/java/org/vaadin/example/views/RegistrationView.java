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

/**
 * The RegistrationView class provides the user interface for the registration page of the application.
 * Users can enter a username and password to create a new account. If the registration is successful,
 * the user is redirected to the login page. Otherwise, error notifications are shown for invalid input or 
 * if the username is already taken.
 * 
 * <p>This class extends {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It uses Vaadin components like {@link com.vaadin.flow.component.textfield.TextField},
 * {@link com.vaadin.flow.component.textfield.PasswordField}, and {@link com.vaadin.flow.component.button.Button} to create 
 * an interactive registration form.</p>
 * 
 * <p>The {@code @Route} annotation maps this view to the "register" URL path, allowing users to access it by navigating to 
 * "/register" in their browser.</p>
 * 
 * <p>This class interacts with the {@link org.vaadin.example.service.UserService} to handle user registration logic.</p>
 * 
 * @see org.vaadin.example.service.UserService
 */
@Route("register")
public class RegistrationView extends VerticalLayout {

    private final UserService userService;

    /**
     * Constructs a new RegistrationView and initializes the registration form components and layout.
     * 
     * @param userService the service used to manage user registration and validation
     */
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
