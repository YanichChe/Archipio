package ru.ccfit.nsu.chernovskaya.Archipio.security.services;

import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

public interface VerificationTokenService {

    String createToken(User user, Boolean deleteUser);
    String getEmail(String verificationToken);
    void deleteTokens(String email);
    void deletedExpiredTokens();
}