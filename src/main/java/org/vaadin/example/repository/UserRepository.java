package org.vaadin.example.repository;

import org.vaadin.example.model.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*Purpose:
The UserRepository is responsible for interacting with the database. 
It provides methods for performing CRUD (Create, Read, Update, Delete) operations on the User entity.

Functionality:

Extends Spring Data JPA's JpaRepository or CrudRepository interface.
Inherits several methods for interacting with the database, such as save(), findAll(), findById(), delete(), etc.
Can include custom query methods based on method naming conventions or JPQL (Java Persistence Query Language). */



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
