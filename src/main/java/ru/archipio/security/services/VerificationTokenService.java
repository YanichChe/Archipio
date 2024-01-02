package ru.archipio.security.services;

import ru.archipio.user.models.User;

public interface VerificationTokenService {

    String createToken(User user, Boolean deleteUser);
    String getEmail(String verificationToken);
    void deleteTokens(String email);
    void deletedExpiredTokens();
}