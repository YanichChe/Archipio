package ru.archipio.security.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.archipio.security.models.RefreshToken;
import ru.archipio.user.models.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndExpiredTimeAfter(String token, Date time);
    Optional<RefreshToken> findByUserAndExpiredTimeAfter(User user, Date time);
    void deleteAllByExpiredTimeBefore(Date time);
}