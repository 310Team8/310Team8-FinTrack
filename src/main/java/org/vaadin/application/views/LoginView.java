package org.vaadin.application.views;

import org.vaadin.application.model.User;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

/**
 * The LoginView class provides the user interface for the login page of the
 * application.
 * Users can enter their username and password to log into the application. If
 * the login
 * is successful, the user is redirected to the dashboard. Otherwise, an error
 * notification
 * is shown. The view also provides a link to navigate to the registration page.
 * 
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It uses various Vaadin components like
 * {@link com.vaadin.flow.component.textfield.TextField},
 * {@link com.vaadin.flow.component.textfield.PasswordField}, and
 * {@link com.vaadin.flow.component.button.Button} to create an interactive
 * login form.
 * </p>
 * 
 * <p>
 * The {@code @Route} annotation maps this view to the root URL path, meaning it
 * is the first
 * page users see when they access the application.
 * </p>
 * 
 * <p>
 * This class interacts with the following services:
 * {@link org.vaadin.application.service.UserService}
 * for managing user authentication, and
 * {@link org.vaadin.application.service.SessionService} for managing
 * session-related data.
 * </p>
 * 
 * @see org.vaadin.application.service.UserService
 * @see org.vaadin.application.service.SessionService
 */

@Route("")
public class LoginView extends VerticalLayout {

    private final transient UserService userService;
    private final transient SessionService sessionService;

    /**
     * Constructs a new LoginView and initializes the login form components and
     * layout.
     * 
     * @param userService    the service used to manage user authentication
     * @param sessionService the service used to manage session-related data
     */

    public LoginView(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;

        addClassName("login-view");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create a logo header
        H1 logo = new H1("FinTrack");
        logo.addClassName("logo");

        // Create overlay box
        Div overlayBox = new Div();
        overlayBox.addClassName("overlay-box");

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

        VerticalLayout formLayout = new VerticalLayout(logo, username, password, loginButton, registerButton);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.STRETCH);

        overlayBox.add(formLayout);
        add(overlayBox);
    }
}
