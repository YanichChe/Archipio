package ru.archipio.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.archipio.user.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIgnoreCase(@NonNull String login);
    Optional<User> findByEmail(@NonNull String email);
    boolean existsByEmail(@NonNull String email);

    long deleteByEmail(@NonNull String email);
}