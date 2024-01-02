package ru.archipio.project.service;

import ru.archipio.project.dtos.ProjectDTO;
import ru.archipio.user.models.User;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getAllUserProjects(User user, String userLogin) throws IOException;
    List<ProjectDTO> getAllPublicProjects() throws IOException;

    ProjectDTO getProject(String projectTitle);
    ProjectDTO createProject(User user, ProjectDTO projectDTO) throws IOException;
    ProjectDTO updateProject(User user, ProjectDTO projectDTO) throws IOException;
    void deleteProject(User user, String projectTitle);
}
