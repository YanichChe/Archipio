package ru.ccfit.nsu.chernovskaya.Archipio.profile.services;

import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeLoginDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeMainImageDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangePasswordDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

public interface ProfileService {
    UserDTO getProfile(User user);
    UserDTO changeLogin(User user, ChangeLoginDTO changeLoginDTO);
    void changePassword(User user, ChangePasswordDTO changePasswordDTO);
    UserDTO changeMainImage(User user, ChangeMainImageDTO changeMainImageDTO);
}