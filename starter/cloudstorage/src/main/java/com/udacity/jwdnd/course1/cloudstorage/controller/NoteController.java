package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/add-note")
    public String addOrUpdateNote(@ModelAttribute Note note, Model model, Authentication auth) {
        Optional<Integer> noteId = Optional.ofNullable(note.getNoteId());
        if (noteId.isEmpty()) {
            return displayResult(model, noteService.addNote(note, userService.getUserById(auth.getName())));
        } else return displayResult(model, noteService.updateNote(note));
    }

    @GetMapping("/delete-note/{noteId:.+}")
    public String deleteNote(@PathVariable Integer noteId, Model model) {
        return displayResult(model, noteService.deleteNote(noteId));
    }

    private String displayResult(Model model, int rowAffected) {
        model.addAttribute(rowAffected == 1 ? "successMsg" : "errorMsg", true);
        return "result";
    }
}