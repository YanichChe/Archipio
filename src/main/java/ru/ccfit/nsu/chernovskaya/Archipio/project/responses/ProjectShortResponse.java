package ru.ccfit.nsu.chernovskaya.Archipio.project.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectShortResponse {
    private String title;
    private String description;
    private String owner;
    private List<String> tags;
    private MultipartFile mainImage;
    private boolean visibility;
}
