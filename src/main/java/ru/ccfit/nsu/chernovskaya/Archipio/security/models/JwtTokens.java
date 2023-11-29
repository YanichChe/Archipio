package ru.ccfit.nsu.chernovskaya.Archipio.security.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokens {
    private String accessToken;
    private String refreshToken;
}