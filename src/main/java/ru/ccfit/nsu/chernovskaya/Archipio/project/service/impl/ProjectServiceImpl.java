package ru.ccfit.nsu.chernovskaya.Archipio.project.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.exceptions.ProjectNotFoundException;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.ProjectRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.repositories.TagRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final FileRepository fileRepository;
    private final ProjectMapper projectMapper;

    /**
     * Возвращает список проектов определенного пользователя. Если пользователь, отправивший запрос, совпадает с
     * пользователем, по которому идет поиск, то возвращает полный список, в том числе и с приватными проектами.
     *
     * @param user пользователь, чьи проекта мы ищем
     * @param userLogin пользователь, отправивший запрос на получение проектов
     * @return список проектов определенного пользователя
     * @throws IOException ошибка при работе с файлами
     */
    @Override
    public List<ProjectDTO> getAllUserProjects(User user, String userLogin) throws IOException {

        List<ProjectDTO> projectDTOS= new ArrayList<>();

        List<Project> result;
        if (Objects.equals(user.getLogin(), userLogin)) {
            result = projectRepository.findByOwner(user);
        } else {
            result=  projectRepository.findByOwner_LoginAndVisibilityTrue(userLogin);
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

        log.info(project.toString());
        Project savedProject = projectRepository.save(project);

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
        ProjectDTO projectDTO  = new ProjectDTO();
        projectMapper.map(project, projectDTO);

        return projectDTO;
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

    private void setParams(ProjectDTO projectDTO, Project project, User owner) {

        List<Tag> projectTags = new ArrayList<>();
        if (projectDTO.getTags() != null) {

            for (String tag : projectDTO.getTags()) {
                projectTags.add(saveTag(tag));
            }
        }

        List<File> files = new ArrayList<>();

        if (projectDTO.getFiles() != null) {
            for (UUID uuid: projectDTO.getFiles()) {
                files.add(fileRepository.findById(uuid).get());
            }
        }

        log.info(projectDTO.toString());
        project.setDescription(projectDTO.getDescription());
        project.setOwner(owner);
        project.setTags(projectTags);
        project.setTitle(projectDTO.getTitle());
        project.setVisibility(projectDTO.isVisibility());
        project.setLikes(0);
        project.setViews(0);
        if (projectDTO.getMainImage() != null) project.setMainImage(projectDTO.getMainImage());
        else project.setMainImage(fileRepository.findByName("/home/tc/archipio/" + "default.jpg").get().getId());
    }
}
