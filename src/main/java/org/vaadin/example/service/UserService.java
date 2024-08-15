package org.vaadin.example.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.User;
import org.vaadin.example.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String plainPassword) {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User();
        user.setName(name);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User loginUser(String name, String plainPassword) {
        User user = userRepository.findByName(name);
        if (user != null && BCrypt.checkpw(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }
}
