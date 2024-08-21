package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.Note;
import org.vaadin.example.repository.NoteRepository;

import java.util.Arrays;
import java.util.List;

public class NoteServiceTests {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNotesByUserId() {
        Note note = new Note();
        note.setUserId(1L);
        Note note2 = new Note();
        note2.setUserId(1L);

        when(noteRepository.findByUserId(1L)).thenReturn(Arrays.asList(note, note2));

        List<Note> notes = noteService.getNotesByUserId(1L);

        assertEquals(2, notes.size());
        assertEquals(note, notes.get(0));
        assertEquals(note2, notes.get(1));
        verify(noteRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testAddNote() {
        Note note = new Note();
        note.setId(1L);
        
        //Should we be returning the note when added to verify creation?
        noteService.addNote(note);

        verify(noteRepository, times(1)).save(note);
    }

    @Test
    public void testDeleteNoteById() {
        Note note = new Note();
        note.setId(1L);

        noteService.deleteNoteById(1L);

        verify(noteRepository, times(1)).deleteById(1L);
    }
    
}
