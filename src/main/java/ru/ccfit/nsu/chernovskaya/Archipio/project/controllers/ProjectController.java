package ru.ccfit.nsu.chernovskaya.Archipio.project.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectShortResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectMapper projectMapper;
    private final ProjectService projectService;

    @GetMapping(name = "/get-full-project")
    ResponseEntity<ProjectFullResponse> showFullProject(@RequestParam String projectTitle) {
        ProjectDTO projectDTO = projectService.getProject(projectTitle);
        ProjectFullResponse projectFullResponse = new ProjectFullResponse();
        projectMapper.map(projectDTO, projectFullResponse);
        return ResponseEntity.ok().body(projectFullResponse);
    }

    @PutMapping("/update-project")
    public ResponseEntity<?> updateProject(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws IOException {
        ProjectDTO projectDTO = new ProjectDTO();
        projectMapper.map(projectCreateRequest, projectDTO, user.getLogin());

        ProjectDTO projectDTO_ = projectService.updateProject(user, projectDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/create-project")
    public ResponseEntity<?> createProject(@AuthenticationPrincipal User user,
                                                             @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws IOException {
        ProjectDTO projectDTO = new ProjectDTO();

        log.info(projectCreateRequest.toString());
        projectMapper.map(projectCreateRequest, projectDTO, user.getLogin());

        ProjectDTO projectDTO_ = projectService.createProject(user, projectDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<List<ProjectShortResponse>> getAllUserProjects(@AuthenticationPrincipal User user, String userLogin)
            throws IOException {
        List<ProjectDTO> projectDTOS = projectService.getAllUserProjects(user, userLogin);
        List<ProjectShortResponse> projectShortResponses = new ArrayList<>();
        for (ProjectDTO projectDTO: projectDTOS) {
            ProjectShortResponse projectShortResponse = new ProjectShortResponse();
            projectMapper.map(projectDTO, projectShortResponse);
            projectShortResponses.add(projectShortResponse);
        }

        return ResponseEntity.status(HttpStatus.OK).body(projectShortResponses);
    }
}
