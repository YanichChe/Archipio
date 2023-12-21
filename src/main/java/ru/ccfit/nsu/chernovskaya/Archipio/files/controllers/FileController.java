package ru.ccfit.nsu.chernovskaya.Archipio.files.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.files.exceptions.FileNotFoundException;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.files.response.UploadFileResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.files.services.FileService;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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

    @PostMapping(value="/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UploadFileResponse> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                               @RequestParam("project") String projectTitle) throws IOException {

        UUID uuid = fileService.load(file, projectTitle);

        return ResponseEntity.ok().body(new UploadFileResponse(uuid));
    }
}
