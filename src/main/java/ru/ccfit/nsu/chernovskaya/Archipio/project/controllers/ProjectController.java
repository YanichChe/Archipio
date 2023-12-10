package ru.ccfit.nsu.chernovskaya.Archipio.project.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectShortResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @GetMapping(name = "/get-short-project")
    ResponseEntity<ProjectShortResponse> showShortProject(@RequestParam String projectTitle) {
        ProjectDTO projectDTO = projectService.getProject(projectTitle);
        ProjectShortResponse projectShortResponse = new ProjectShortResponse();
        projectMapper.map(projectDTO, projectShortResponse);
        return ResponseEntity.ok().body(projectShortResponse);
    }

    @GetMapping(name = "/get-full-project")
    ResponseEntity<ProjectFullResponse> showFullProject(@RequestParam String projectTitle) {
        ProjectDTO projectDTO = projectService.getProject(projectTitle);
        ProjectFullResponse projectFullResponse = new ProjectFullResponse();
        projectMapper.map(projectDTO, projectFullResponse);
        return ResponseEntity.ok().body(projectFullResponse);
    }

    @GetMapping(
            value = "/get-image-with-jpeg-type",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("uuid") UUID uuid) throws IOException {
        Optional<File> file = fileRepository.findById(uuid);
        Path filePath = Paths.get(uploadDirectory + file.get().getName());
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            InputStream in = Files.newInputStream(filePath, StandardOpenOption.READ);
            log.info(uploadDirectory + file.get().getName());
            return in.readAllBytes();
        } else {
            log.info(uploadDirectory + file.get().getName());
            return null;
        }
    }

    @PutMapping("/update-project")
    public ResponseEntity<ProjectFullResponse> updateProject(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws IOException {
        ProjectDTO projectDTO = new ProjectDTO();
        projectMapper.map(projectCreateRequest, projectDTO, user.getLogin());

        ProjectDTO projectDTO_ = projectService.updateProject(user, projectDTO);
        ProjectFullResponse projectFullResponse = new ProjectFullResponse();
        projectMapper.map(projectDTO_, projectFullResponse);

        return ResponseEntity.status(HttpStatus.OK).body(projectFullResponse);
    }

    @PostMapping("/create-project")
    public ResponseEntity<ProjectFullResponse> createProject(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws IOException {
        ProjectDTO projectDTO = new ProjectDTO();
        projectMapper.map(projectCreateRequest, projectDTO, user.getLogin());

        ProjectDTO projectDTO_ = projectService.createProject(user, projectDTO);
        ProjectFullResponse projectFullResponse = new ProjectFullResponse();
        projectMapper.map(projectDTO_, projectFullResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectFullResponse);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal User user,
                                                             @RequestBody String projectTitle) {

        projectService.deleteProject(user, projectTitle);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get-all-public-projects")
    public ResponseEntity<List<ProjectShortResponse>> getAllPublicProjects() throws IOException {
        List<ProjectDTO> projectDTOS = projectService.getAllPublicProjects();
        List<ProjectShortResponse> projectShortResponses = new ArrayList<>();
        for (ProjectDTO projectDTO: projectDTOS) {
            ProjectShortResponse projectShortResponse = new ProjectShortResponse();
            projectMapper.map(projectDTO, projectShortResponse);
            projectShortResponses.add(projectShortResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectShortResponses);
    }

    @GetMapping("/get-all-user-projects")
    public ResponseEntity<List<ProjectShortResponse>> getAllUserProjects(@AuthenticationPrincipal User user, User user_)
            throws IOException {
        List<ProjectDTO> projectDTOS = projectService.getAllUserProjects(user, user_);
        List<ProjectShortResponse> projectShortResponses = new ArrayList<>();
        for (ProjectDTO projectDTO: projectDTOS) {
            ProjectShortResponse projectShortResponse = new ProjectShortResponse();
            projectMapper.map(projectDTO, projectShortResponse);
            projectShortResponses.add(projectShortResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectShortResponses);
    }

}
