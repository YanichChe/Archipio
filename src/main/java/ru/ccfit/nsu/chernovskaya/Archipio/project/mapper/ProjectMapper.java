package ru.ccfit.nsu.chernovskaya.Archipio.project.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.FileLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProjectMapper {

    private final FileLoader fileLoader;

    public void map(Project project, ProjectDTO projectDTO) throws IOException {

        List<MultipartFile> files = new ArrayList<>();
        for (ru.ccfit.nsu.chernovskaya.Archipio.project.models.File file: project.getFiles()) {
            files.add(map(file));
        }

        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setOwner(project.getOwner().getLogin());
        projectDTO.setTags(map(project.getTags()));
        projectDTO.setLikes(project.getLikes());
        projectDTO.setViews(project.getViews());
        projectDTO.setFiles(files);
        projectDTO.setVisibility(project.isVisibility());
        projectDTO.setMainImage(fileLoader.getFileByName(project.getMainImage()));
    }

    private List<String> map(List<Tag> tags) {
        List<String> tagsStr = new ArrayList<>();
        for (Tag tag: tags) {
            tagsStr.add(tag.getTitle());
        }

        return tagsStr;
    }

    private MultipartFile map(File file) throws IOException {
        return fileLoader.getFileByName(file.getName());
    }

    public void map(ProjectCreateRequest projectCreateRequest, ProjectDTO projectDTO, String userName) {
        projectDTO.setTitle(projectCreateRequest.getTitle());
        projectDTO.setDescription(projectCreateRequest.getDescription());
        projectDTO.setOwner(userName);
        projectDTO.setTags(projectCreateRequest.getTags());
        projectDTO.setFiles(projectCreateRequest.getFiles());
        projectDTO.setMainImage(projectCreateRequest.getMainImage());
        projectDTO.setVisibility(projectCreateRequest.isVisibility());
    }
}
