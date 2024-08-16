package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.User;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String password) {
        User existingUser = userService.findUserByName(name);
        if (existingUser != null) {
            return new ResponseEntity<>("Username already taken.", HttpStatus.CONFLICT);
        }
        userService.registerUser(name, password);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String name, @RequestParam String password) {
        User user = userService.loginUser(name, password);
        if (user != null) {
            sessionService.setLoggedInUserId(user.getId());
            return new ResponseEntity<>("Login successful.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        sessionService.logout();
        return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
