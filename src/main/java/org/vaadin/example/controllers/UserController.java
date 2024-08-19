package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.User;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

/**
 * Rest controller for managing user-related operations.
 * This controller provides endpoints for user registration, login, logout, 
 * and retrieving user details by ID.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    /**
     * Registers a new user with the given name and password.
     *
     * @param name     the name of the user to register
     * @param password the password of the user to register
     * @return a ResponseEntity containing a success message and HTTP status code
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String password) {
        User existingUser = userService.findUserByName(name);
        if (existingUser != null) {
            return new ResponseEntity<>("Username already taken.", HttpStatus.CONFLICT);
        }
        userService.registerUser(name, password);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    /**
     * Authenticates a user with the given name and password.
     *
     * @param name     the name of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return a ResponseEntity containing a success message and HTTP status code if successful,
     * or an error message and HTTP status code if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String name, @RequestParam String password) {
        User user = userService.loginUser(name, password);
        if (user != null) {
            sessionService.setLoggedInUserId(user.getId());
            return new ResponseEntity<>("Login successful.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @return a ResponseEntity containing a success message and HTTP status code
     */
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        sessionService.logout();
        return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the user object and HTTP status code if found,
     * or an HTTP status code if the user is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
