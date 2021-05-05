package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.AssertTrue;
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
        int userById = userService.getUserById(auth.getName());
        Optional<Integer> noteId = Optional.ofNullable(note.getNoteId());
        if (noteId.isEmpty()) {
            return addUniqueNoteWithValidDescription(note, model, userById);
        } else if (isValidDescriptionLength(note)) {
            return updateUniqueNoteWithValidDescription(note, model, userById);
        } else {
            return displayErrorMsgForNote(model, "Note can't be saved as description exceed 1000 characters.");
        }
    }

    @GetMapping("/delete-note/{noteId:.+}")
    public String deleteNote(@PathVariable Integer noteId, Model model) {
        return displayResult(model, noteService.deleteNote(noteId));
    }

    private String displayResult(Model model, int rowAffected) {
        model.addAttribute(rowAffected == 1 ? "successMsg" : "errorMsg", true);
        return "result";
    }

    private String displayErrorMsgForNote(Model model, String message) {
        model.addAttribute("specificError", message);
        return "result";
    }

    @AssertTrue
    public boolean isValidDescriptionLength(Note note) {
        return note.getNoteDescription().length() <= 1000;
    }

    private String addUniqueNoteWithValidDescription(Note note, Model model, int userById) {
        if (isValidDescriptionLength(note)) {
            if (noteService.isUniqueNote(userById, note.getNoteTitle(), note.getNoteDescription())) {
                return displayResult(model, noteService.addNote(note, userById));
            } else {
                return displayErrorMsgForNote(model, "Note already available.");
            }
        } else {
            return displayErrorMsgForNote(model, "Note can't be saved as description exceed 1000 characters.");
        }
    }

    private String updateUniqueNoteWithValidDescription(Note note, Model model, int userById) {
        if (noteService.isUniqueNote(userById, note.getNoteTitle(), note.getNoteDescription())) {
            return displayResult(model, noteService.updateNote(note));
        }
        return displayErrorMsgForNote(model, "Note already available.");
    }
}