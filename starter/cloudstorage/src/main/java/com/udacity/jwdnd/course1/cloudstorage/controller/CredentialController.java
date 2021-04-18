package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    @Autowired
    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/add-credential")
    public String addOrUpdateCredential(@ModelAttribute Credential credential, Model model, Authentication auth) {
        Optional<Integer> credentialId = Optional.ofNullable(credential.getCredentialId());
        if (credentialId.isEmpty()) {
            credential.setUserId(userService.getUserById(auth.getName()));
            return displayResult(model, credentialService.addCredential(credential));
        } else {
            return displayResult(model, credentialService.updateCredential(credential));
        }
    }

    @GetMapping("/delete-credential/{credentialId:.+}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {
        return displayResult(model, credentialService.deleteCredential(credentialId));
    }

    private String displayResult(Model model, int rowAffected) {
        model.addAttribute(rowAffected == 1 ? "successMsg" : "errorMsg", true);
        return "result";
    }
}
