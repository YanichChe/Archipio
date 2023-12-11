package ru.ccfit.nsu.chernovskaya.Archipio.auth.services.impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.dtos.AuthDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.services.AuthService;
import ru.ccfit.nsu.chernovskaya.Archipio.auth.services.EmailService;
import ru.ccfit.nsu.chernovskaya.Archipio.security.dto.TokensDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.CustomUserDetails;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.JwtTokens;
import ru.ccfit.nsu.chernovskaya.Archipio.security.repositories.RolesRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.repositories.UserDetailsRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.security.services.JwtTokenService;
import ru.ccfit.nsu.chernovskaya.Archipio.security.services.VerificationTokenService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.exceptions.UserAlreadyExistException;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;
import ru.ccfit.nsu.chernovskaya.Archipio.user.repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final VerificationTokenService verificationTokenService;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    @Value("${default.image.uuid}")
    private UUID defaultImageUUID;

    @Override
    public UserDTO register(@Valid UserDTO userDTO) throws MessagingException, IOException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.info("User " + userDTO.getEmail() + " already exists");
            throw new UserAlreadyExistException(
                    "User " + userDTO.getEmail() + " already exists");
        }

        User user = modelMapper.map(userDTO, User.class);
        if (user.getProfilePic() == null) {
            user.setProfilePic(defaultImageUUID);
        }
        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUser(savedUser);
        userDetails.setRoleList(List.of(rolesRepository.findById(1L).orElseThrow()));
        userDetails.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDetails.setIsActive(false);

        CustomUserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        log.info("User {} registered", savedUser.getEmail());

        String verificationToken = verificationTokenService.createToken(savedUser, true);
        emailService.sendActivateAccount(savedUser.getEmail(), verificationToken);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public void verify(String verificationToken) {
        String email = verificationTokenService.getEmail(verificationToken);
        CustomUserDetails userDetails = userDetailsRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
        userDetails.setIsActive(true);

        userDetailsRepository.save(userDetails);
        log.info("User {} activated", email);

        verificationTokenService.deleteTokens(email);
    }

    @Override
    public TokensDTO login(@Valid AuthDTO authDTO) {
        User user = userRepository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User " + authDTO.getEmail() + " not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
        log.info("User {} logged in", authDTO.getEmail());

        JwtTokens tokens = jwtTokenService.createTokens(user);
        return modelMapper.map(tokens, TokensDTO.class);
    }

    @Override
    public TokensDTO refreshTokens(String refreshToken) {
        JwtTokens tokens = jwtTokenService.refreshTokens(refreshToken);
        return modelMapper.map(tokens, TokensDTO.class);
    }

    @Override
    public UserDTO resetPassword(@Valid AuthDTO authDTO) throws MessagingException, IOException {
        CustomUserDetails userDetails = userDetailsRepository
                .findByUserEmail(authDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User " + authDTO.getEmail() + " not found"));

        userDetails.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        userDetails.setIsActive(false);

        CustomUserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        log.info("User {} reset password", savedUserDetails.getUser().getEmail());

        String verificationToken = verificationTokenService.createToken(savedUserDetails.getUser(),
                false);
        emailService.sendConfirmPasswordChange(savedUserDetails.getUser().getEmail(),
                verificationToken);

        return modelMapper.map(savedUserDetails.getUser(), UserDTO.class);
    }
}