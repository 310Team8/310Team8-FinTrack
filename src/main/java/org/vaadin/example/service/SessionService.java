package org.vaadin.example.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {

    private final HttpSession session;

    public SessionService(HttpSession session) {
        this.session = session;
    }

    public void setLoggedInUserId(Long userId) {
        session.setAttribute("userId", userId);
    }

    public Long getLoggedInUserId() {
        return (Long) session.getAttribute("userId");
    }

    public void invalidateSession() {
        session.invalidate();
    }
}
