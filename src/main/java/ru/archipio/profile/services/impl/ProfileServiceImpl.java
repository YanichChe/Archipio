package ru.archipio.profile.services.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.archipio.files.models.File;
import ru.archipio.files.repositories.FileRepository;
import ru.archipio.files.services.FileService;
import ru.archipio.profile.dtos.ChangeLoginDTO;
import ru.archipio.profile.dtos.ChangeMainImageDTO;
import ru.archipio.profile.dtos.ChangePasswordDTO;
import ru.archipio.profile.services.ProfileService;
import ru.archipio.security.models.CustomUserDetails;
import ru.archipio.security.repositories.UserDetailsRepository;
import ru.archipio.user.dtos.UserDTO;
import ru.archipio.user.models.User;
import ru.archipio.user.repositories.UserRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final FileRepository fileRepository;

    /**
     * @param user пользователь, сделавший запрос на обновление профиля
     * @return обновленный пользователь.
     */
    @Override
    public UserDTO getProfile(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * @param user пользователь, сделавший запрос на обновление имени
     * @param changeLoginDTO запрос на обновление имени
     * @return обновленный пользователь
     */
    @Override
    public UserDTO changeLogin(User user, @Valid ChangeLoginDTO changeLoginDTO) {
        log.debug(changeLoginDTO.getLogin());
        user.setLogin(changeLoginDTO.getLogin());
        User savedUser = userRepository.save(user);
        return  modelMapper.map(savedUser, UserDTO.class);
    }

    /**
     * @param user пользователь, сделавший запрос на обновление пароля
     * @param changePasswordDTO запрос на обновление пароля
     */
    @Override
    public void changePassword(User user, @Valid ChangePasswordDTO changePasswordDTO) {
        CustomUserDetails customUserDetails = userDetailsRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + user.getEmail() + "not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), customUserDetails.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        customUserDetails.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userDetailsRepository.save(customUserDetails);
    }

    /**
     * @param user пользователь, сделавший запрос на обновление иконки профиля
     * @param multipartFile запрос на обновление иконки профиля
     * @return обновленный пользователь
     */
    @Override
    public UserDTO changeMainImage(User user,  UUID multipartFile) throws IOException {
        user.setProfilePic(multipartFile);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }
}
