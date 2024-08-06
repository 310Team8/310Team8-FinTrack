package org.vaadin.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/* The model package contains data model classes that represent entities in the application. 
These classes are typically annotated with JPA annotations to map them to database tables. 

The User class is annotated with @Entity, indicating that it is a JPA entity. The id field is annotated with @Id and @GeneratedValue, 
specifying it as the primary key and that its value is generated automatically.
*/

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
