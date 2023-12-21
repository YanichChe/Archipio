package ru.ccfit.nsu.chernovskaya.Archipio.files.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {
    UUID load(MultipartFile multipartFile, String projectTitle) throws IOException;
    MultipartFile getFileUUID(UUID uuid) throws IOException;
}
