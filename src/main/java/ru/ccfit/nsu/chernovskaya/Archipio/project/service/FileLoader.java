package ru.ccfit.nsu.chernovskaya.Archipio.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileLoader {
    String load(MultipartFile multipartFile) throws IOException;
    MultipartFile getFileByName(String fileName) throws IOException;
}
