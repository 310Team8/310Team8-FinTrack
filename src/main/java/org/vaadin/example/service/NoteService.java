package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Note;
import org.vaadin.example.repository.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    public void addNote(Note note) {
        noteRepository.save(note);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
