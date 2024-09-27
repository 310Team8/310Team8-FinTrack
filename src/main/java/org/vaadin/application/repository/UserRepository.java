package org.vaadin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.User;

/**
 * Repository interface for managing {@link User} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and
 * custom queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique name.
     *
     * @param name the name of the user to find
     * @return the user with the specified name, or null if no user is found
     */
    User findByName(String name);
}
