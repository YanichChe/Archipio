package ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDTO {
    private String newPassword;
    private String oldPassword;
}
