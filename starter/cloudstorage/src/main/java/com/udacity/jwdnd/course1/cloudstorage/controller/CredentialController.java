package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.commons.validator.routines.UrlValidator;
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
        int userById = userService.getUserById(auth.getName());
        Optional<Integer> credentialId = Optional.ofNullable(credential.getCredentialId());
        if (credentialId.isEmpty()) {
            return addCredentialForValidUrlAndUserName(credential, model, userById);
        } else if (isValidUrlCredential(credential.getUrl())) {
            return updateCredentialForValidUrlAndUserName(credential, model, userById);
        }
        return displayErrorMsgForCredential(model, "Url is not a valid value.");
    }

    @GetMapping("/delete-credential/{credentialId:.+}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {
        return displayResult(model, credentialService.deleteCredential(credentialId));
    }

    private String displayResult(Model model, int rowAffected) {
        model.addAttribute(rowAffected == 1 ? "successMsg" : "errorMsg", true);
        return "result";
    }

    private String displayErrorMsgForCredential(Model model, String message) {
        model.addAttribute("specificError", message);
        return "result";
    }

    private boolean isValidUrlCredential(String urlCredential) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(urlCredential);
    }

    private String addCredentialForValidUrlAndUserName(Credential credential, Model model, int userById) {
        if (isValidUrlCredential(credential.getUrl())) {
            if (credentialService.isUniqueUserName(userById, credential.getUsername())) {
                credential.setUserId(userById);
                return displayResult(model, credentialService.addCredential(credential));
            } else {
                return displayErrorMsgForCredential(model, "User already available.");
            }
        } else {
            return displayErrorMsgForCredential(model, "Url is not a valid value.");
        }
    }
    private String updateCredentialForValidUrlAndUserName(Credential credential, Model model, int userById) {
        if (credentialService.isUniqueUserName(userById, credential.getUsername())) {
            return displayResult(model, credentialService.updateCredential(credential));
        }
        return displayErrorMsgForCredential(model, "User already available.");
    }
}
