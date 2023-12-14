package ru.ccfit.nsu.chernovskaya.Archipio.profile.services.impl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.files.repositories.FileRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.files.services.FileService;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeLoginDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeMainImageDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangePasswordDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.services.ProfileService;
import ru.ccfit.nsu.chernovskaya.Archipio.security.models.CustomUserDetails;
import ru.ccfit.nsu.chernovskaya.Archipio.security.repositories.UserDetailsRepository;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;
import ru.ccfit.nsu.chernovskaya.Archipio.user.repositories.UserRepository;

import java.io.IOException;

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
    public UserDTO changeMainImage(User user,  MultipartFile multipartFile) throws IOException {
        String fileName = fileService.load(multipartFile);
        File file = new File();
        file.setName(fileName);
        File savedFile = fileRepository.save(file);
        user.setProfilePic(savedFile.getId());
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }
}
