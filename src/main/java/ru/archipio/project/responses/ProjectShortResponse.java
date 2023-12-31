package ru.archipio.project.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectShortResponse {
    private String title;
    private String description;
    private String owner;
    private List<String> tags;
    private UUID mainImage;
    private boolean visibility;
}
