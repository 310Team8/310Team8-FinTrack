package org.vaadin.example.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.User;
import org.vaadin.example.service.UserService;

/*Purpose: Controllers in a Spring Boot application handle HTTP requests 
and map them to the appropriate business logic. They serve as the intermediaries 
between the client and the backend services.

Functionality: They process incoming requests, handle input validation, a
nd return responses. Controllers often interact with services to perform business 
operations and then return the results to the client, typically in the form of JSON or XML. */

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userService.findAllUsers();
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.saveUser(user);
  }
}
