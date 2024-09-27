package org.vaadin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.Note;

import java.util.List;

/**
 * Repository interface for managing {@link Note} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Finds a list of notes associated with a specific user ID.
     *
     * @param userId the ID of the user whose notes are to be retrieved
     * @return a list of notes associated with the specified user ID
     */
    List<Note> findByUserId(Long userId);
}
