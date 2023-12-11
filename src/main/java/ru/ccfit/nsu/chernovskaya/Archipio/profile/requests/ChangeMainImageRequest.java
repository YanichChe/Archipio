package ru.ccfit.nsu.chernovskaya.Archipio.profile.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeMainImageRequest {
    private MultipartFile mainImage;
}
