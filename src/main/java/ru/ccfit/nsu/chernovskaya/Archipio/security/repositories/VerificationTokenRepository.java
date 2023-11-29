package ru.ccfit.nsu.chernovskaya.Archipio.security.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.VerificationToken;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

@Repository
public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByTokenAndExpiredTimeAfter(String token, Date time);
    Optional<VerificationToken> findByUserAndExpiredTimeAfter(User user, Date time);
    List<VerificationToken> findAllByExpiredTimeBefore(Date time);
    void deleteByUserEmail(String email);
}