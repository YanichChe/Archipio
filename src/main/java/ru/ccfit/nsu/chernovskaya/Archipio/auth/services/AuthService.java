package ru.ccfit.nsu.chernovskaya.Archipio.auth.services;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.dtos.AuthDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.security.dto.TokensDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;

import java.io.IOException;

public interface AuthService {

    UserDTO register(@Valid UserDTO userDTO) throws MessagingException, IOException;
    void verify(String activationToken);
    TokensDTO login(@Valid AuthDTO authDTO);
    TokensDTO refreshTokens(String refreshToken);
    UserDTO resetPassword(@Valid AuthDTO authDTO) throws MessagingException, IOException;
}