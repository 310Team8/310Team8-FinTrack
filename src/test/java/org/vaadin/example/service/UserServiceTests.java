package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.User;
import org.vaadin.example.repository.UserRepository;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setName("testuser");
        user.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser("testuser", "testpassword");

        assertNotNull(registeredUser);
        assertEquals(user.getName(), registeredUser.getName());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void testLoginUser() {
        User user = new User();
        user.setName("testuser");
        user.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));

        Mockito.when(userRepository.findByName("testuser")).thenReturn(user);

        User authenticatedUser = userService.loginUser("testuser", "testpassword");

        assertNotNull(authenticatedUser);
        assertEquals(user.getName(), authenticatedUser.getName());
        assertEquals(user.getPassword(), authenticatedUser.getPassword());
    }

    @Test
    void testUserLoginInvalidPassword() {
        User user = new User();
        user.setName("testuser");
        user.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));

        Mockito.when(userRepository.findByName("testuser")).thenReturn(user);

        User authenticatedUser = userService.loginUser("testuser", "wrongpassword");

        assertEquals(null, authenticatedUser);
    }

    @Test
    void testUserLoginNonexistentUser() {
        Mockito.when(userRepository.findByName("testuser")).thenReturn(null);

        User authenticatedUser = userService.loginUser("testuser", "testpassword");
        assertEquals(null, authenticatedUser);
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.findUserById(1L);

        assertEquals(user, foundUser);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testFindUserByName() {
        User user = new User();
        user.setName("testuser");
        user.setPassword(BCrypt.hashpw("testpassword", BCrypt.gensalt()));

        Mockito.when(userRepository.findByName("testuser")).thenReturn(user);

        User foundUser = userService.findUserByName("testuser");

        assertEquals(user, foundUser);
        Mockito.verify(userRepository, Mockito.times(1)).findByName("testuser");
    }

}
