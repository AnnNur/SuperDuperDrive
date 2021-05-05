package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File getFileById(int fileId) {
        return fileMapper.findFile(fileId);
    }

    public int addFile(MultipartFile fileUpload, int userId) throws IOException {

        return fileMapper.addFile(new File(
                null,
                fileUpload.getOriginalFilename(),
                fileUpload.getContentType(),
                Long.toString(fileUpload.getSize()),
                fileUpload.getBytes(),
                userId));
    }

    public int deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public boolean isUniqueFileName(Integer UserId, String FileName) {
        Optional<File> file = Optional.ofNullable(fileMapper.uniqueFileName(UserId, FileName));
        return (file.isEmpty());
    }

    public List<File> getFilesByUserId(int userId) {
        return fileMapper.getFilesByUserId(userId);
    }
}

