package ru.ccfit.nsu.chernovskaya.Archipio.auth.controllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.dtos.AuthDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.requests.LoginRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.requests.ResetPasswordRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.responses.TokensResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.services.AuthService;
import ru.ccfit.nsu.chernovskaya.Archipio.security.dto.TokensDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.requests.RegisterRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.responses.ProfileResponse;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException, IOException {
        authService.register(modelMapper.map(registerRequest, UserDTO.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokensDTO tokensDTO = authService.login(modelMapper.map(loginRequest, AuthDTO.class));
        return ResponseEntity.ok()
                .body(modelMapper.map(tokensDTO, TokensResponse.class));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam(name = "token") String verificationToken) {
        authService.verify(verificationToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh-tokens")
    public ResponseEntity<TokensResponse> refreshTokens(@RequestParam(name = "token") String refreshToken) {
        TokensDTO tokensDTO = authService.refreshTokens(refreshToken);
        return ResponseEntity.ok()
                .body(modelMapper.map(tokensDTO, TokensResponse.class));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody LoginRequest resetPasswordRequest) throws MessagingException, IOException {
        authService.resetPassword(modelMapper.map(resetPasswordRequest, AuthDTO.class));
        return ResponseEntity.ok().build();
    }
}