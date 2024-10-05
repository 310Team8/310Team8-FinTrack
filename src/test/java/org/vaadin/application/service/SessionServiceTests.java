package org.vaadin.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.application.service.SessionService;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class SessionServiceTests {

    @Mock
    private VaadinSession vaadinSession;

    @InjectMocks
    private SessionService sessionService;

    private MockedStatic<VaadinSession> vaadinSessionMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vaadinSessionMockedStatic = Mockito.mockStatic(VaadinSession.class);
        vaadinSessionMockedStatic.when(VaadinSession::getCurrent).thenReturn(vaadinSession);
    }

    @Test
    void testSetLoggedInUserId() {
        sessionService.setLoggedInUserId(1L);

        verify(vaadinSession).setAttribute("userId", 1L);
    }

    @Test
    void testGetLoggedInUserId() {
        Long userId = 1L;
        when(vaadinSession.getAttribute("userId")).thenReturn(userId);
        Long retrievedId = sessionService.getLoggedInUserId();

        assertEquals(userId, retrievedId);
        verify(vaadinSession, times(1)).getAttribute("userId");
    }

    @Test
    void testLogout() {
        WrappedSession wrappedSession = Mockito.mock(WrappedSession.class);
        when(vaadinSession.getSession()).thenReturn(wrappedSession);
        sessionService.logout();

        verify(wrappedSession, times(1)).invalidate();
        verify(vaadinSession, times(1)).close();
    }

    @AfterEach
    void tearDown() {
        vaadinSessionMockedStatic.close();
    }
}
