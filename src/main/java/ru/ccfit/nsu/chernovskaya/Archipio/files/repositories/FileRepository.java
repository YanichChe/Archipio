package ru.ccfit.nsu.chernovskaya.Archipio.files.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    Optional<File> findByName(String name);

}
