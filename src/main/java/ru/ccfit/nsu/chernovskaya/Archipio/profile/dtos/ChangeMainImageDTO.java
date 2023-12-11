package ru.ccfit.nsu.chernovskaya.Archipio.profile.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeMainImageDTO {
    private MultipartFile newImage;
}
