package ru.ccfit.nsu.chernovskaya.Archipio.project.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.FileLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileLoaderImpl implements FileLoader {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String load(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);

        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();

    }

    @Override
    public MultipartFile getFileByName(String fileName) throws IOException {
        Path filePath = Path.of(uploadDir, fileName);
        byte[] fileBytes = Files.readAllBytes(filePath);
        return new MockMultipartFile(fileName, fileBytes);
    }
}
