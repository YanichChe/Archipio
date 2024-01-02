package ru.archipio.auth.services;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import ru.archipio.auth.dtos.AuthDTO;
import ru.archipio.security.dto.TokensDTO;
import ru.archipio.user.dtos.UserDTO;

import java.io.IOException;

public interface AuthService {

    UserDTO register(@Valid UserDTO userDTO) throws MessagingException, IOException;
    void verify(String activationToken);
    TokensDTO login(@Valid AuthDTO authDTO);
    TokensDTO refreshTokens(String refreshToken);
    UserDTO resetPassword(@Valid AuthDTO authDTO) throws MessagingException, IOException;
}