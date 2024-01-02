package ru.archipio.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.archipio.project.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
