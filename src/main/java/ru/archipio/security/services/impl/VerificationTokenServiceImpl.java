package ru.archipio.security.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.archipio.security.exceptions.VerificationTokenNotFoundException;
import ru.archipio.security.models.VerificationToken;
import ru.archipio.security.repositories.VerificationTokenRepository;
import ru.archipio.security.services.VerificationTokenService;
import ru.archipio.user.models.User;
import ru.archipio.user.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Value("${verification.token.expired-time}")
    private Long EXPIRED_TIME;

    @Override
    public String createToken(User user, Boolean deleteUser) {
        VerificationToken token = verificationTokenRepository
                .findByUserAndExpiredTimeAfter(user, new Date())
                .orElse(new VerificationToken());
        token.setUser(user);
        token.setExpiredTime(new Date(System.currentTimeMillis() + EXPIRED_TIME));
        token.setToken(UUID.randomUUID().toString());
        token.setDeleteUser(deleteUser);

        VerificationToken savedToken = verificationTokenRepository.save(token);
        return savedToken.getToken();
    }

    @Override
    public String getEmail(String verificationToken) {
        VerificationToken token = verificationTokenRepository
                .findByTokenAndExpiredTimeAfter(verificationToken, new Date())
                .orElseThrow(() -> new VerificationTokenNotFoundException("Verification token " + verificationToken + " not found"));
        return token.getUser()
                .getEmail();
    }

    @Override
    public void deleteTokens(String email) {
        verificationTokenRepository.deleteByUserEmail(email);
    }

    @Override
    @Async
    @Scheduled(fixedDelayString = "${verification.token.expired-time}")
    public void deletedExpiredTokens() {
        List<VerificationToken> tokens = verificationTokenRepository
                .findAllByExpiredTimeBefore(new Date());
        for (VerificationToken token : tokens) {
            verificationTokenRepository.delete(token);
            if (token.getDeleteUser()) {
                userRepository.delete(token.getUser());
                log.info("User {} did not activate the account after registration and was deleted",
                        token.getUser().getEmail());
            }
        }
    }
}