package ru.archipio.files.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.archipio.files.exceptions.FileNotFoundException;
import ru.archipio.files.models.File;
import ru.archipio.files.repositories.FileRepository;
import ru.archipio.files.response.UploadFileResponse;
import ru.archipio.files.services.FileService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final FileRepository fileRepository;
    private final FileService fileService;

    @GetMapping(value = "/{uuid}",  produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getFile(@PathVariable String uuid) throws MalformedURLException {

        Optional<File> file = fileRepository.findById(UUID.fromString(uuid));
        if (file.isEmpty()) {
            throw new FileNotFoundException(UUID.fromString(uuid));
        }

        Path filePath = Paths.get(file.get().getName());
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .body(resource);
    }

    @PostMapping(value="/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UploadFileResponse> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        UUID uuid = fileService.load(file);

        return ResponseEntity.ok().body(new UploadFileResponse(uuid));
    }
}
