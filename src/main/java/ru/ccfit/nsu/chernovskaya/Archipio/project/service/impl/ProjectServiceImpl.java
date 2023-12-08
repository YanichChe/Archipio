package ru.ccfit.nsu.chernovskaya.Archipio.project.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.exceptions.ProjectNotFoundException;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.ProjectRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.TagRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.FileLoader;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final FileRepository fileRepository;
    private final ProjectMapper projectMapper;
    private final FileLoader fileLoader;

    @Override
    public List<ProjectDTO> getAllUserProjects(User user) {
        return null;
    }

    @Override
    public List<ProjectDTO> getAllPublicProjects() {
        return null;
    }

    @Override
    public ProjectDTO createProject(User user, ProjectDTO projectDTO) throws IOException {
        Project project = new Project();

        List<Tag> projectTags = new ArrayList<>();
        if (projectDTO.getTags() != null) {

            for (String tag : projectDTO.getTags()) {
                projectTags.add(saveTag(tag));
            }
        }


        List<File> projectFiles = new ArrayList<>();
        if (projectDTO.getFiles() != null) {
            for (MultipartFile file : projectDTO.getFiles()) {
                fileLoader.load(file);
                projectFiles.add(saveFile(file.getName()));
            }
        }

        if (projectDTO.getMainImage() != null) {
            fileLoader.load(projectDTO.getMainImage());
        }

        project.setDescription(projectDTO.getDescription());
        project.setOwner(user);
        project.setTags(projectTags);
        project.setTitle(projectDTO.getTitle());
        project.setFiles(projectFiles);
        project.setVisibility(projectDTO.isVisibility());
        project.setLikes(0);
        project.setViews(0);
        project.setMainImage("");

        Project savedProject = projectRepository.save(project);

        ProjectDTO projectDTO1 = new ProjectDTO();

        projectMapper.map(savedProject, projectDTO1);
        return projectDTO1;
    }

    @Override
    public ProjectDTO updateProject(User user, ProjectDTO projectDTO) {
        return null;
    }

    @Override
    public ProjectDTO getProject(String projectTitle) {
        Project project = projectRepository.findByTitleLike(projectTitle).orElseThrow(() ->
                new ProjectNotFoundException("Project with title" + projectTitle + "not found"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public void deleteProject(User user, int projectId) {

    }

    private Tag saveTag(String tag) {
        Tag newTag = new Tag();
        newTag.setTitle(tag);
        tagRepository.save(newTag);
        return newTag;
    }

    private File saveFile(String title) {
        File newFile = new File();
        newFile.setName(title);
        fileRepository.save(newFile);
        return newFile;
    }
}
