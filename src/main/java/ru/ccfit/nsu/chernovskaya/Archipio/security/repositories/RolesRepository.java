package ru.ccfit.nsu.chernovskaya.Archipio.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {}
