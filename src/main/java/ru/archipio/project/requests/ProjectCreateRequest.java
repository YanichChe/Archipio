package ru.archipio.project.requests;

import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectCreateRequest {
    private String title;
    private String description;
    private List<String> tags;
    private List<UUID> files;
    private boolean visibility;
    private String mainImage;
}
