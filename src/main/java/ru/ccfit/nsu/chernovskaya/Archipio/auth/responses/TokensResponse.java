package ru.ccfit.nsu.chernovskaya.Archipio.auth.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponse {

    @NotBlank(message = "Access token cannot be null or whitespace")
    private String accessToken;

    @NotBlank(message = "Refresh token cannot be null or whitespace")
    private String refreshToken;
}