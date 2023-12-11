package ru.ccfit.nsu.chernovskaya.Archipio.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDTO {
    private String title;
    private String description;
    private String owner;
    private List<String> tags = new ArrayList<>();
    private long likes = 0;
    private long views = 0;
    private List<MultipartFile> files = new ArrayList<>();
    private MultipartFile mainImage;
    private boolean visibility;

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", tags=" + tags +
                ", likes=" + likes +
                ", views=" + views +
                ", files=" + files +
                ", mainImage=" + mainImage +
                ", visibility=" + visibility +
                '}';
    }
}
