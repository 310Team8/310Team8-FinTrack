package org.vaadin.example.model;

import jakarta.persistence.*;

/**
 * Entity representing a note.
 * A note contains content and is associated with a user by user ID.
 */
@Entity
public class Note {
   /**
     * The unique identifier for the note.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the user associated with this note.
     */
    private Long userId;

    /**
     * The content of the note.
     */
    private String content;

    // Getters and Setters

    /**
     * Gets the unique identifier for the note.
     *
     * @return the ID of the note
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the note.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the user associated with this note.
     *
     * @return the user ID associated with the note
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with this note.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the content of the note.
     *
     * @return the content of the note
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the note.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
}
