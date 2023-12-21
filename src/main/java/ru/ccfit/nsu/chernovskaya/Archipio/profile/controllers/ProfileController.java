package ru.ccfit.nsu.chernovskaya.Archipio.profile.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeLoginDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangeMainImageDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos.ChangePasswordDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.requests.ChangeLoginRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.requests.ChangeMainImageRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.requests.ChangePasswordRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.responses.ProfileResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.profile.services.ProfileService;
import ru.ccfit.nsu.chernovskaya.Archipio.project.dtos.ProjectDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.project.mapper.ProjectMapper;
import ru.ccfit.nsu.chernovskaya.Archipio.project.requests.ProjectCreateRequest;
import ru.ccfit.nsu.chernovskaya.Archipio.project.responses.ProjectFullResponse;
import ru.ccfit.nsu.chernovskaya.Archipio.project.service.ProjectService;
import ru.ccfit.nsu.chernovskaya.Archipio.user.dtos.UserDTO;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;

    @GetMapping("/show")
    ResponseEntity<ProfileResponse> showProfile(@AuthenticationPrincipal User user) {
        ProfileResponse profileResponse = new ProfileResponse(user.getLogin(), user.getEmail(), user.getProfilePic());
        return ResponseEntity.ok().body(profileResponse);
    }

    @PutMapping("/edit/login")
    public ResponseEntity<ProfileResponse> changeName(@AuthenticationPrincipal User user,
                                                      @Valid @RequestBody ChangeLoginRequest changeLoginRequest) {
        profileService.changeLogin(user, modelMapper.map(changeLoginRequest, ChangeLoginDTO.class));
        ProfileResponse profileResponse = new ProfileResponse(user.getLogin(), user.getEmail(), user.getProfilePic());
        return ResponseEntity.ok().body(profileResponse);
    }

    @PutMapping("/edit/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        profileService.changePassword(user, modelMapper.map(changePasswordRequest, ChangePasswordDTO.class));
        return ResponseEntity.ok().build();
    }


    @PutMapping(value = "/edit/main-image", consumes = {"multipart/form-data"})
    public ResponseEntity<ProfileResponse> changeMainImage(@AuthenticationPrincipal User user,
                                                           @RequestBody UUID multipartFile) throws IOException {
        profileService.changeMainImage(user, multipartFile);
        ProfileResponse profileResponse = new ProfileResponse(user.getLogin(), user.getEmail(), user.getProfilePic());
        return ResponseEntity.ok().body(profileResponse);
    }
}
