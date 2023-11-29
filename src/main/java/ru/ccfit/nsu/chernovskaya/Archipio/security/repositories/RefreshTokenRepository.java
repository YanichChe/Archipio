package ru.ccfit.nsu.chernovskaya.Archipio.security.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.RefreshToken;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndExpiredTimeAfter(String token, Date time);
    Optional<RefreshToken> findByUserAndExpiredTimeAfter(User user, Date time);
    void deleteAllByExpiredTimeBefore(Date time);
}