package ru.ccfit.nsu.chernovskaya.Archipio.user.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Login cannot be empty")
    @Size(max = 70, message = "Login cannot be more than 70 symbols")
    private String login;

    @NotEmpty(message = "Email cannot be empty")
    @Size(max = 80, message = "Email length cannot be more than 80 symbols")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Email has invalid format")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 80, message = "Password length must be less than or equal to 80 symbols")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$",
            message = "Password must contain numbers and letters and not contain whitespaces. " +
                    "Password length must be more than or equal to 8 symbols")
    private String password;

    private String profilePic;
}
