package ru.ccfit.nsu.chernovskaya.Archipio.project.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectShortResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    public void map(Project project, ProjectDTO projectDTO) {

        List<UUID> files = new ArrayList<>();
        for (File file: project.getFiles()) {
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
        projectDTO.setMainImage(project.getMainImage());
    }

    private List<String> map(List<Tag> tags) {
        List<String> tagsStr = new ArrayList<>();
        for (Tag tag: tags) {
            tagsStr.add(tag.getTitle());
        }

        return tagsStr;
    }

    private UUID map(File file) {
        return file.getId();
    }

    public void map(ProjectCreateRequest projectCreateRequest, ProjectDTO projectDTO, String userName) {
        projectDTO.setTitle(projectCreateRequest.getTitle());
        projectDTO.setDescription(projectCreateRequest.getDescription());
        projectDTO.setOwner(userName);
        projectDTO.setTags(projectCreateRequest.getTags());
        if (projectCreateRequest.getFiles() != null) projectDTO.setFiles(projectCreateRequest.getFiles());
        if (projectCreateRequest.getMainImage() != null) projectDTO.setMainImage(projectCreateRequest.getMainImage());
        projectDTO.setVisibility(projectCreateRequest.isVisibility());
    }

    public void map (ProjectDTO projectDTO, ProjectFullResponse projectFullResponse) {

        projectFullResponse.setTitle(projectDTO.getTitle());
        projectFullResponse.setDescription(projectDTO.getDescription());
        projectFullResponse.setOwner(projectDTO.getOwner());
        projectFullResponse.setTags(projectDTO.getTags());
        projectFullResponse.setLikes(projectDTO.getLikes());
        projectFullResponse.setViews(projectDTO.getViews());
        projectFullResponse.setVisibility(projectDTO.isVisibility());
        projectFullResponse.setFiles(projectDTO.getFiles());
        projectFullResponse.setMainImage(projectDTO.getMainImage());

    }

    public void map (ProjectDTO projectDTO, ProjectShortResponse projectShortResponse) {

        projectShortResponse.setTitle(projectDTO.getTitle());
        projectShortResponse.setDescription(projectDTO.getDescription());
        projectShortResponse.setOwner(projectDTO.getOwner());
        projectShortResponse.setTags(projectDTO.getTags());
        projectShortResponse.setVisibility(projectDTO.isVisibility());
        projectShortResponse.setMainImage(projectDTO.getMainImage());

    }
}
