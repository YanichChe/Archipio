package ru.ccfit.nsu.chernovskaya.Archipio.files.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ccfit.nsu.chernovskaya.Archipio.files.exceptions.FileNotFoundException;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final FileRepository fileRepository;

    @GetMapping("/{uuid}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable UUID uuid) throws MalformedURLException {
        Optional<File> file = fileRepository.findById(uuid);
        if (file.isEmpty()) {
            throw new FileNotFoundException(uuid);
        }

        Path filePath = Paths.get(uploadDirectory + file.get().getName());
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.get().getName() + "\"").body(resource);
    }
}
