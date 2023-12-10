package ru.ccfit.nsu.chernovskaya.Archipio.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileLoader {
    String load(MultipartFile multipartFile) throws IOException;
    MultipartFile getFileUUID(UUID uuid) throws IOException;
}
