package ru.ccfit.nsu.chernovskaya.Archipio.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitleLike(@NonNull String title);
    List<Project> findByVisibilityTrue();

    List<Project> findByOwner(User owner);

    List<Project> findByOwner_LoginAndVisibilityTrue(String login);


}
