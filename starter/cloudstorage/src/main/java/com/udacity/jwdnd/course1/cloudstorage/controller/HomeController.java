package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
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
    private final NoteService noteService;
    private final FileService fileService;


    private List<Credential> credentials;
    private List<Note> notes;
    private List<File> files;
    List<String> decryptedPasswords;

    public HomeController(UserService userService, EncryptionService encryptionService, CredentialService credentialService,
                          NoteService noteService, FileService fileService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
    }

    @PostConstruct
    public void postConstruct() {
        credentials = new ArrayList<>();
        notes = new ArrayList<>();
        files = new ArrayList<>();
        decryptedPasswords = new ArrayList<>();
    }

    @GetMapping()
    public String getHome(Model model, Authentication auth) {
        Integer userById = userService.getUserById(auth.getName());

        credentials = credentialService.getCredentialsByUserId(userById);
        notes = noteService.getNotesByUserId(userById);
        files = fileService.getFilesByUserId(userById);
        decryptedPasswords = credentialService.getDecryptedPasswords(userById);

        model.addAttribute("credential", new Credential());
        model.addAttribute("note", new Note());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentialService", credentialService);
        model.addAttribute("credentials", credentials);
        model.addAttribute("notes", notes);
        model.addAttribute("files",files);

        return "home";
    }
}
