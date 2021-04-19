package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file,
                             Authentication auth, Model model) throws IOException {

        int userId = userService.getUserById(auth.getName());
        if (!file.isEmpty()) {
            if (fileService.isUniqueFileName(userId, file.getOriginalFilename())) {
                return displayResult(model, fileService.addFile(file, userId));
            } else {
                return displayErrorMsgForFile(model, "Name of the uploaded file already exists.");
            }
        } else {
            return displayErrorMsgForFile(model, "Please browse for a file before clicking the Upload button.");
        }
    }

    @GetMapping("/view-file/{fileId:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable int fileId) {
        File file = fileService.getFileById(fileId);

        InputStream inputStream = new ByteArrayInputStream(file.getFileData());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @GetMapping("/delete-file/{fileId:.+}")
    public String deleteFile(@PathVariable Integer fileId, Model model) {
        return displayResult(model, fileService.deleteFile(fileId));
    }

    private String displayResult(Model model, int rowAffected) {
        model.addAttribute(rowAffected == 1 ? "successMsg" : "errorMsg", true);
        return "result";
    }

    private String displayErrorMsgForFile(Model model, String errorMsg) {
        model.addAttribute("fileError", errorMsg);
        return "result";
    }
}
