package ru.ccfit.nsu.chernovskaya.Archipio.files.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.files.services.FileService;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.ProjectRepository;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;

    @Override
    public UUID load(MultipartFile file, String projectTitle) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(uploadDir + file.getName()));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        int i = 0;
        StringBuilder stringBuilder = new StringBuilder(file.getName());
        while (fileRepository.findByName(uploadDir + stringBuilder).isPresent()) {
            stringBuilder.append(i++);
        }

        File newFile = new File();
        newFile.setName(uploadDir + stringBuilder);
        newFile.setProject(projectRepository.findByTitleLike(projectTitle).get());
        File savedFile = fileRepository.save(newFile);

        log.info(String.valueOf(savedFile.getId()));
        return savedFile.getId();
    }

    @Override
    public MultipartFile getFileUUID(UUID uuid) throws IOException {
        Optional<File> file = fileRepository.findById(uuid);
        Path filePath = Path.of(uploadDir, file.get().getName());
        byte[] fileBytes = Files.readAllBytes(filePath);
        return new MockMultipartFile(file.get().getName(), fileBytes);
    }
}
