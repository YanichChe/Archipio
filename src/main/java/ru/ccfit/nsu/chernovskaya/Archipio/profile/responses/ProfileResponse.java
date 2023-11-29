package ru.ccfit.nsu.chernovskaya.Archipio.profile.responses;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    @NotBlank(message = "Login cannot be null or whitespace")
    private String login;

    @NotBlank(message = "Email cannot be null or whitespace")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Email has invalid format")
    private String email;

    private String mainImage;
}