package org.vaadin.application.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.User;
import org.vaadin.application.repository.UserRepository;

/**
 * Service class for managing user-related operations.
 * This class provides methods for user registration, login, and retrieval of
 * user details.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user with a hashed password.
     *
     * @param name          the name of the user to register
     * @param plainPassword the plain text password of the user to be hashed and
     *                      stored
     * @return the newly registered user object
     */
    public User registerUser(String name, String plainPassword) {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User();
        user.setName(name);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    /**
     * Authenticates a user by checking the provided plain password against the
     * stored hashed password.
     *
     * @param name          the name of the user attempting to log in
     * @param plainPassword the plain text password of the user attempting to log in
     * @return the authenticated user object if the credentials are valid, or null
     *         if invalid
     */
    public User loginUser(String name, String plainPassword) {
        User user = userRepository.findByName(name);
        if (user != null && BCrypt.checkpw(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user object if found, or null if not found
     */
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Finds a user by their name.
     *
     * @param name the name of the user to find
     * @return the user object if found, or null if not found
     */
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }
}
