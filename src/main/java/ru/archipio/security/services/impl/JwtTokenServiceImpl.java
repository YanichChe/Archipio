package ru.archipio.security.services.impl;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.archipio.security.exceptions.JwtTokenException;
import ru.archipio.security.models.JwtTokens;
import ru.archipio.security.models.RefreshToken;
import ru.archipio.security.repositories.RefreshTokenRepository;
import ru.archipio.security.services.JwtTokenService;
import ru.archipio.user.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token.secret}")
    private String SECRET;

    @Value("${jwt.access-token.expired-time}")
    private Long ACCESS_TOKEN_EXPIRED_TIME;

    @Value("${jwt.refresh-token.expired-time}")
    private Long REFRESH_TOKEN_EXPIRED_TIME;

    @PostConstruct
    protected void initSecret() {
        SECRET = Base64.getEncoder()
                .encodeToString(SECRET.getBytes());
    }

    @Override
    public JwtTokens createTokens(User user) {
        return new JwtTokens(createAccessToken(user), createRefreshToken(user));
    }

    @Override
    public String resolveAccessToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public String getEmailFromAccessToken(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new JwtTokenException("Access token not valid or outdated");
        }
    }

    @Override
    public Date getExpirationFromAccessToken(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getExpiration();
        } catch (JwtException e) {
            throw new JwtTokenException("Access token not valid or outdated");
        }
    }

    @Override
    public JwtTokens refreshTokens(String refreshToken) {
        RefreshToken token = refreshTokenRepository
                .findByTokenAndExpiredTimeAfter(refreshToken, new Date())
                .orElseThrow(() -> new JwtTokenException("Refresh token " + refreshToken + " not valid or outdated"));
        return createTokens(token.getUser());
    }

    private String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    private String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository
                .findByUserAndExpiredTimeAfter(user, new Date())
                .orElse(new RefreshToken());
        token.setUser(user);
        token.setExpiredTime(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_TIME));
        token.setToken(UUID.randomUUID().toString());

        RefreshToken savedToken = refreshTokenRepository.save(token);
        return savedToken.getToken();
    }

    @Override
    @Async
    @Scheduled(fixedDelayString = "${jwt.refresh-token.expired-time}")
    public void deletedExpiredRefreshTokens() {
        refreshTokenRepository.deleteAllByExpiredTimeBefore(new Date());
    }
}