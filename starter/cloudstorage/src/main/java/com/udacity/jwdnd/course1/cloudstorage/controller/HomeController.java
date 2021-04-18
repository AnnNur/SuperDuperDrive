package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final EncryptionService encryptionService;
    private final CredentialService credentialService;
    private NoteService noteService;


    private List<Credential> credentials;
    private List<Note> notes;
    List<String> decryptedPasswords;

    public HomeController(UserService userService, EncryptionService encryptionService, CredentialService credentialService,
                          NoteService noteService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.noteService = noteService;
    }

    @PostConstruct
    public void postConstruct() {
        credentials = new ArrayList<>();
        notes = new ArrayList<>();
        decryptedPasswords = new ArrayList<>();
    }

    @GetMapping()
    public String getHome(Model model, Authentication auth) {
        Integer userById = userService.getUserById(auth.getName());

        credentials = credentialService.getCredentialsByUserId(userById);
        notes = noteService.getNotesByUserId(userById);
        decryptedPasswords = credentialService.getDecryptedPasswords(userById);

        model.addAttribute("credential", new Credential());
        model.addAttribute("note", new Note());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentialService", credentialService);
        model.addAttribute("credentials", credentials);
        model.addAttribute("notes", notes);

        return "home";
    }
}
