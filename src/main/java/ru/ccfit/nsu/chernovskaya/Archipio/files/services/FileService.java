package ru.ccfit.nsu.chernovskaya.Archipio.files.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {
    String load(MultipartFile multipartFile) throws IOException;
    MultipartFile getFileUUID(UUID uuid) throws IOException;
}
