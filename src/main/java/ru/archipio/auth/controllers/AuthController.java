package ru.archipio.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.archipio.ApiPath;
import ru.archipio.auth.dtos.AuthDTO;
import ru.archipio.auth.requests.LoginRequest;
import ru.archipio.auth.requests.ResetPasswordRequest;
import ru.archipio.auth.responses.TokensResponse;
import ru.archipio.auth.services.AuthService;
import ru.archipio.security.dto.TokensDTO;
import ru.archipio.user.dtos.UserDTO;
import ru.archipio.auth.requests.RegisterRequest;

import java.io.IOException;

@Tag(name="AuthController", description="Обеспечивает методы для работы с аутенфикацией и авторизацией пользователей")
@RestController
@RequestMapping(ApiPath.AUTH)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Регистрация пользователя"
    )
    @PostMapping(ApiPath.REGISTER)
    public ResponseEntity<Void> register(@Valid @RequestBody @Parameter(description = "Запрос на регистрацию")
                                             RegisterRequest registerRequest) throws MessagingException, IOException {
        authService.register(modelMapper.map(registerRequest, UserDTO.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Аунтенфикация  пользователя"
    )
    @PostMapping(ApiPath.LOGIN)
    public ResponseEntity<TokensResponse> login(@Valid @RequestBody
                                                    @Parameter(description = "Запрос на вход") LoginRequest loginRequest) {
        TokensDTO tokensDTO = authService.login(modelMapper.map(loginRequest, AuthDTO.class));
        return ResponseEntity.ok()
                .body(modelMapper.map(tokensDTO, TokensResponse.class));
    }

    @Operation(
            summary = "Подтверждение аккаунта"
    )
    @GetMapping(ApiPath.VERIFY)
    public ResponseEntity<Void> verify(@RequestParam(name = "token")
                                           @Parameter(description = "Верификационный токен") String verificationToken) {
        authService.verify(verificationToken);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление токена"
    )
    @GetMapping(ApiPath.REFRESH)
    public ResponseEntity<TokensResponse> refreshTokens(@RequestParam(name = "token")
                                                            @Parameter(description = "Обновляющий токен")
                                                            String refreshToken) {
        TokensDTO tokensDTO = authService.refreshTokens(refreshToken);
        return ResponseEntity.ok()
                .body(modelMapper.map(tokensDTO, TokensResponse.class));
    }

    @Operation(
            summary = "Восстановление пароля"
    )
    @PostMapping(ApiPath.RESET)
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody @Parameter(description = "Запрос на восстановление пароля")
            ResetPasswordRequest resetPasswordRequest) throws MessagingException, IOException {
        authService.resetPassword(modelMapper.map(resetPasswordRequest, AuthDTO.class));
        return ResponseEntity.ok().build();
    }
}
