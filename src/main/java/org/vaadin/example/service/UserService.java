package org.vaadin.example.service;

import org.vaadin.example.model.User;
import org.vaadin.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/*The service package contains service classes that encapsulate the business logic. 
These classes interact with the repository layer to perform CRUD operations and apply business rules.

The UserService class is annotated with @Service, making it a Spring-managed service component. 
It uses the UserRepository to perform database operations and apply any necessary business logic.*/

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
