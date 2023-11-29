package ru.ccfit.nsu.chernovskaya.Archipio.auth.services;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {

    void sendMessage(String to, String subject, String text) throws MessagingException, IOException;
    void sendActivateAccount(String to, String token) throws MessagingException, IOException;
    void sendConfirmPasswordChange(String to, String token) throws MessagingException, IOException;
}