package ru.ccfit.nsu.chernovskaya.Archipio.project.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final FileRepository fileRepository;
    private final ProjectMapper projectMapper;
    private final FileLoader fileLoader;

    /**
     * Возвращает список проектов определенного пользователя. Если пользователь, отправивший запрос, совпадает с
     * пользователем, по которому идет поиск, то возвращает полный список, в том числе и с приватными проектами.
     *
     * @param user пользователь, чьи проекта мы ищем
     * @param user_ пользователь, отправивший запрос на получение проектов
     * @return список проектов определенного пользователя
     * @throws IOException ошибка при работе с файлами
     */
    @Override
    public List<ProjectDTO> getAllUserProjects(User user, User user_) throws IOException {
        List<ProjectDTO> projectDTOS= new ArrayList<>();

        List<Project> result;
        if (Objects.equals(user.getId(), user_.getId())) {
            result = projectRepository.findByOwner(user);
        } else {
            result=  projectRepository.findByOwnerAndVisibilityTrue(user);
        }

        for (Project project: result) {
            ProjectDTO newProjectDTO = new ProjectDTO();
            projectMapper.map(project, newProjectDTO);
            projectDTOS.add(newProjectDTO);
        }
        return projectDTOS;
    }

    /**
     * @return все публичные проекты в системе
     * @throws IOException ошибка при работе с файлами
     */
    @Override
    public List<ProjectDTO> getAllPublicProjects() throws IOException {
        List<ProjectDTO> projectDTOS= new ArrayList<>();
        List<Project> result = projectRepository.findByVisibilityTrue();
        for (Project project: result) {
            ProjectDTO newProjectDTO = new ProjectDTO();
            projectMapper.map(project, newProjectDTO);
            projectDTOS.add(newProjectDTO);
        }
        return projectDTOS;
    }

    /**
     * @param user пользователь, который создает проект
     * @param projectDTO проект
     * @return сохраненный проект
     * @throws IOException ошибка при работе с файлами
     */
    @Override
    public ProjectDTO createProject(User user, ProjectDTO projectDTO) throws IOException {
        Project project = new Project();

        setParams(projectDTO, project, user);

        Project savedProject = projectRepository.save(project);
        log.info(savedProject.toString());

        ProjectDTO projectDTO1 = new ProjectDTO();
        projectMapper.map(savedProject, projectDTO1);
        return projectDTO1;
    }

    /**
     * @param user владедец проекта
     * @param projectDTO проект
     * @return обновленный проект
     * @throws IOException ошибка при работе с файлами
     */
    @Override
    public ProjectDTO updateProject(User user, ProjectDTO projectDTO) throws IOException {
        Optional<Project> project = projectRepository.findByTitleLike(projectDTO.getTitle());

        if (project.isEmpty()) throw new ProjectNotFoundException("Project with title " + projectDTO.getTitle()
        + " not found");

        setParams(projectDTO, project.get(), user);

        Project savedProject = projectRepository.save(project.get());
        log.info(savedProject.toString());

        ProjectDTO projectDTO1 = new ProjectDTO();
        projectMapper.map(savedProject, projectDTO1);
        return projectDTO1;
    }

    /**
     * @param projectTitle название проекта
     * @return проект, если он есть в базе, иначе выбрасывается исключение
     */
    @Override
    public ProjectDTO getProject(String projectTitle) {
        Project project = projectRepository.findByTitleLike(projectTitle).orElseThrow(() ->
                new ProjectNotFoundException("Project with title" + projectTitle + "not found"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public void deleteProject(User user, String projectTitle) {
        Optional<Project> project = projectRepository.findByTitleLike(projectTitle);
        if (project.isEmpty()) throw new ProjectNotFoundException("Project with title" + projectTitle + "not found");
        if (project.get().getOwner().equals(user)) {
            projectRepository.delete(project.get());
        }
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

    private void setParams(ProjectDTO projectDTO, Project project, User owner) throws IOException {
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
        project.setOwner(owner);
        project.setTags(projectTags);
        project.setTitle(projectDTO.getTitle());
        project.setFiles(projectFiles);
        project.setVisibility(projectDTO.isVisibility());
        project.setLikes(0);
        project.setViews(0);

        log.info(project.toString());
        if (projectDTO.getMainImage() != null) {
            File mainImage = fileRepository.save(new File(projectDTO.getMainImage().getName()));
            project.setMainImage(mainImage.getId());
        } else {
            project.setMainImage(fileRepository.findByName("default.jpg").get().getId());
        }
    }
}
