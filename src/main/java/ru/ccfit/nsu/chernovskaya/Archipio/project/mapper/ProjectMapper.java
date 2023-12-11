package ru.ccfit.nsu.chernovskaya.Archipio.project.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectShortResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.files.services.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final FileService fileService;
    private final FileRepository fileRepository;

    public void map(Project project, ProjectDTO projectDTO) throws IOException {

        List<MultipartFile> files = new ArrayList<>();
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
        projectDTO.setMainImage(fileService.getFileUUID(project.getMainImage()));
    }

    private List<String> map(List<Tag> tags) {
        List<String> tagsStr = new ArrayList<>();
        for (Tag tag: tags) {
            tagsStr.add(tag.getTitle());
        }

        return tagsStr;
    }

    private MultipartFile map(File file) throws IOException {
        return fileService.getFileUUID(file.getId());
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
