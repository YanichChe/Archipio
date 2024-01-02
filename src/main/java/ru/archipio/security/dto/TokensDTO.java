package ru.archipio.security.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokensDTO {

    @NotEmpty(message = "Access token is empty")
    private String accessToken;

    @NotEmpty(message = "Refresh token is empty")
    private String refreshToken;
}