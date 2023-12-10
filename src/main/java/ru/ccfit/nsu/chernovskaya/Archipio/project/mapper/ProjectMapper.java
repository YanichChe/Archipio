package ru.ccfit.nsu.chernovskaya.Archipio.project.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectShortResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.FileLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final FileLoader fileLoader;
    private final FileRepository fileRepository;

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
        projectDTO.setMainImage(fileLoader.getFileUUID(project.getMainImage()));
    }

    private List<String> map(List<Tag> tags) {
        List<String> tagsStr = new ArrayList<>();
        for (Tag tag: tags) {
            tagsStr.add(tag.getTitle());
        }

        return tagsStr;
    }

    private MultipartFile map(File file) throws IOException {
        return fileLoader.getFileUUID(file.getId());
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

    public void map (ProjectDTO projectDTO, ProjectFullResponse projectFullResponse) {

        List<UUID> filesName = new ArrayList<>();
        map(projectDTO.getFiles(), filesName);

        projectFullResponse.setTitle(projectDTO.getTitle());
        projectFullResponse.setDescription(projectDTO.getDescription());
        projectFullResponse.setOwner(projectDTO.getOwner());
        projectFullResponse.setTags(projectDTO.getTags());
        projectFullResponse.setLikes(projectDTO.getLikes());
        projectFullResponse.setViews(projectDTO.getViews());
        projectFullResponse.setVisibility(projectDTO.isVisibility());
        projectFullResponse.setFiles(filesName);
        projectFullResponse.setMainImage(map(projectDTO.getMainImage()));

    }

    public void map (ProjectDTO projectDTO, ProjectShortResponse projectShortResponse) {

        List<UUID> filesName = new ArrayList<>();
        map(projectDTO.getFiles(), filesName);

        projectShortResponse.setTitle(projectDTO.getTitle());
        projectShortResponse.setDescription(projectDTO.getDescription());
        projectShortResponse.setOwner(projectDTO.getOwner());
        projectShortResponse.setTags(projectDTO.getTags());
        projectShortResponse.setVisibility(projectDTO.isVisibility());
        projectShortResponse.setMainImage(map(projectDTO.getMainImage()));

    }



    private void map(List<MultipartFile> files, List<UUID> filesName) {
        for (MultipartFile file: files) {
            Optional<File> file_ = fileRepository.findByName(file.getName());
            filesName.add(file_.get().getId());
        }
    }

    private UUID map(MultipartFile file) {
        Optional<File> file_ = fileRepository.findByName(file.getName());
        return file_.get().getId();
    }
}
