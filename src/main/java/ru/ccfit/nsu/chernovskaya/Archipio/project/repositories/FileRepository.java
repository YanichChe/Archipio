package ru.ccfit.nsu.chernovskaya.Archipio.project.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Tag;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    Optional<File> findByName(String name);

}
