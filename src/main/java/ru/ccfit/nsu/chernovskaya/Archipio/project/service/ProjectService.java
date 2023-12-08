package ru.ccfit.nsu.chernovskaya.Archipio.project.service;

import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getAllUserProjects(User user);
    List<ProjectDTO> getAllPublicProjects();

    ProjectDTO getProject(String projectTitle);
    ProjectDTO createProject(User user, ProjectDTO projectDTO) throws IOException;
    ProjectDTO updateProject(User user, ProjectDTO projectDTO);
    void deleteProject(User user, int projectId);
}
