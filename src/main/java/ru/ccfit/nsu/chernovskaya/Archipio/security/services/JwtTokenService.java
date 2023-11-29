package ru.ccfit.nsu.chernovskaya.Archipio.security.services;

import jakarta.servlet.http.HttpServletRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.JwtTokens;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.Date;

public interface JwtTokenService {
    JwtTokens createTokens(User user);
    String resolveAccessToken(HttpServletRequest request);
    String getEmailFromAccessToken(String accessToken);
    Date getExpirationFromAccessToken(String accessToken);
    JwtTokens refreshTokens(String refreshToken);
    void deletedExpiredRefreshTokens();
}