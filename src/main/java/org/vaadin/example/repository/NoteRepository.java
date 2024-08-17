package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserId(Long userId);
}
