package org.vaadin.application.service;

import org.springframework.stereotype.Service;
import com.vaadin.flow.server.VaadinSession;

/**
 * Service class for managing user sessions.
 * This class provides methods to set and retrieve the logged-in user's ID,
 * and to handle user logout by invalidating the session.
 */
@Service
public class SessionService {

    /**
     * Sets the logged-in user's ID in the current Vaadin session.
     *
     * @param userId the ID of the user to set in the session
     */
    public void setLoggedInUserId(Long userId) {
        VaadinSession.getCurrent().setAttribute("userId", userId);
    }

    /**
     * Retrieves the logged-in user's ID from the current Vaadin session.
     *
     * @return the ID of the logged-in user, or null if no user is logged in
     */
    public Long getLoggedInUserId() {
        return (Long) VaadinSession.getCurrent().getAttribute("userId");
    }

    /**
     * Logs out the current user by invalidating the session and closing the Vaadin
     * session.
     */
    public void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }
}
