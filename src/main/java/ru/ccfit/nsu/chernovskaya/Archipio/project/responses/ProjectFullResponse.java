package ru.ccfit.nsu.chernovskaya.Archipio.project.responses;

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
public class ProjectFullResponse {
    private String title;
    private String description;
    private String owner;
    private List<String> tags;
    private long likes;
    private long views;
    private List<UUID> files;
    private UUID mainImage;
    private boolean visibility;
}
