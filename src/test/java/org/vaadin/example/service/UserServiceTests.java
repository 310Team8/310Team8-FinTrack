package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        String name = "John Doe";
        String plainPassword = "password123";
        User user = new User();
        user.setName(name);
        user.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(name, plainPassword);

        assertNotNull(registeredUser);
        assertEquals(name, registeredUser.getName());
        assertTrue(BCrypt.checkpw(plainPassword, registeredUser.getPassword()));

        verify(userRepository, times(1)).save(any(User.class));
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
