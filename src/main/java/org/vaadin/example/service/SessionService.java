package org.vaadin.example.service;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.VaadinSession;

@Service
public class SessionService {

    public void setLoggedInUserId(Long userId) {
        VaadinSession.getCurrent().setAttribute("userId", userId);
    }

    public Long getLoggedInUserId() {
        return (Long) VaadinSession.getCurrent().getAttribute("userId");
    }

    public void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }
}
