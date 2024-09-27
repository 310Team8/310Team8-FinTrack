package org.vaadin.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.Note;
import org.vaadin.application.repository.NoteRepository;

import java.util.List;

/**
 * Service class for managing note-related operations.
 * This class interacts with the {@link NoteRepository} to perform CRUD
 * operations on {@link Note} entities.
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Retrieves a list of notes associated with a specific user ID.
     *
     * @param userId the ID of the user whose notes are to be retrieved
     * @return a list of notes associated with the specified user ID
     */
    public List<Note> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    /**
     * Adds a new note to the repository.
     *
     * @param note the note object to be added
     */
    public void addNote(Note note) {
        noteRepository.save(note);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id the ID of the note to be deleted
     */
    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
