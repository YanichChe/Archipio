package ru.ccfit.nsu.chernovskaya.Archipio.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.CustomUserDetails;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Long> {
    Optional<CustomUserDetails> findByUserEmail(String email);
}
