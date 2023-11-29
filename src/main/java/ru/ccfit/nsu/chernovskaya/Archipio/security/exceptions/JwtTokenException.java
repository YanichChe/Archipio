package ru.ccfit.nsu.chernovskaya.Archipio.security.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }
}