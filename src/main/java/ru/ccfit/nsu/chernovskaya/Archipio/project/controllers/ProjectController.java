package ru.ccfit.nsu.chernovskaya.Archipio.project.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectMapper  projectMapper;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

//    @GetMapping(name = "/get-short-project")
//    ResponseEntity<ProjectShortResponse> showShortProject(@RequestParam String projectTitle) {
//        ProjectDTO projectDTO = projectService.getProject(projectTitle);
//        ProjectShortResponse projectShortResponse = new ProjectShortResponse();
//        projectMapper.map(projectDTO, projectShortResponse);
//        return ResponseEntity.ok().body(projectShortResponse);
//    }

    /*@GetMapping(name = "/get-full-project")
    ResponseEntity<ProjectFullResponse> showFullProject(@RequestParam  String projectTitle) {
        ProjectDTO projectDTO = projectService.getProject(projectTitle);
        return ResponseEntity.ok().body(modelMapper.map(projectDTO, ProjectFullResponse.class));
    }
*/
    @PostMapping("/create-project")
    public ResponseEntity<ProjectFullResponse> createProject(@AuthenticationPrincipal User user,
                                                           @Valid @RequestBody ProjectCreateRequest projectCreateRequest) throws IOException {
        ProjectDTO projectDTO = new ProjectDTO();
        projectMapper.map(projectCreateRequest, projectDTO, user.getLogin());

        ProjectDTO projectDTO_ = projectService.createProject(user, projectDTO);
        ProjectFullResponse projectFullResponse = new ProjectFullResponse();
        modelMapper.map(projectDTO_, projectFullResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectFullResponse);
    }
}
