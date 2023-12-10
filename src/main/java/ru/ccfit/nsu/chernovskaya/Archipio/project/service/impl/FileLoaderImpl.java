package ru.ccfit.nsu.chernovskaya.Archipio.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.FileLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileLoaderImpl implements FileLoader {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final FileRepository fileRepository;

    @Override
    public String load(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);

        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();

    }

    @Override
    public MultipartFile getFileUUID(UUID uuid) throws IOException {
        Optional<File> file = fileRepository.findById(uuid);
        Path filePath = Path.of(uploadDir, file.get().getName());
        byte[] fileBytes = Files.readAllBytes(filePath);
        return new MockMultipartFile(file.get().getName(), fileBytes);
    }
}
