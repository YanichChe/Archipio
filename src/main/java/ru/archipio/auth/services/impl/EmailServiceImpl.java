package ru.archipio.auth.services.impl;

import jakarta.mail.MessagingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.archipio.auth.services.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${application.url}")
    private String appUrl;

    @SneakyThrows
    @Async
    public void sendMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(mimeMessage);
        log.info("Letter \"{}\" was sent to {}", subject, to);
    }

    @Override
    public void sendActivateAccount(String to, String token) {
        Context context = new Context();
        context.setVariables(Map.of("host", appUrl,
                "token", token));

        String htmlText = templateEngine.process("activation-account", context);

        sendMessage(to, "Welcome and Account activation", htmlText);
    }

    @Override
    public void sendConfirmPasswordChange(String to, String token) {
        Context context = new Context();
        context.setVariables(Map.of("username", to,
                "host", appUrl,
                "token", token));

        String htmlText = templateEngine.process("confirm-password-change", context);

        sendMessage(to, "Confirm password change", htmlText);
    }
}