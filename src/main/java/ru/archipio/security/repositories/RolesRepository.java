package ru.archipio.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.archipio.security.models.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {}
