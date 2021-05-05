package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    @Autowired
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int addNote(Note note, int userId) {
        return noteMapper.addNote(new Note(0, note.getNoteTitle(), note.getNoteDescription(), userId));
    }

    public int updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    public int deleteNote(Integer noteId) {
        return noteMapper.deleteNote(noteId);
    }

    public boolean isUniqueNote(Integer userId, String noteTitle, String noteDescription) {
        Optional<Note> note = Optional.ofNullable(noteMapper.uniqueNote(userId, noteTitle, noteDescription));
        return (note.isEmpty());
    }

    public List<Note> getNotesByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

}
