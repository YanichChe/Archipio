package ru.ccfit.nsu.chernovskaya.Archipio.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
