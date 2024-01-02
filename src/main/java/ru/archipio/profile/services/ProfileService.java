package ru.archipio.profile.services;

import org.springframework.web.multipart.MultipartFile;
import ru.archipio.profile.dtos.ChangeLoginDTO;
import ru.archipio.profile.dtos.ChangeMainImageDTO;
import ru.archipio.profile.dtos.ChangePasswordDTO;
import ru.archipio.user.dtos.UserDTO;
import ru.archipio.user.models.User;

import java.io.IOException;
import java.util.UUID;

public interface ProfileService {
    UserDTO getProfile(User user);
    UserDTO changeLogin(User user, ChangeLoginDTO changeLoginDTO);
    void changePassword(User user, ChangePasswordDTO changePasswordDTO);
    UserDTO changeMainImage(User user, UUID multipartFile) throws IOException;
}
