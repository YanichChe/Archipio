package ru.ccfit.nsu.chernovskaya.Archipio.project.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class ProjectDTO {
    private String title;
    private String description;
    private String owner;
    private List<String> tags = new ArrayList<>();
    private long likes = 0;
    private long views = 0;
    private List<UUID> files = new ArrayList<>();
    private UUID mainImage;
    private boolean visibility;
}
