package ru.ccfit.nsu.chernovskaya.Archipio.project.requests;

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
public class ProjectCreateRequest {
    private String title;
    private String description;
    private List<String> tags;
    private List<MultipartFile> files;
    private MultipartFile mainImage;
    private boolean visibility;

    @Override
    public String toString() {
        return "ProjectCreateRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", files=" + files +
                ", mainImage=" + mainImage +
                ", visibility=" + visibility +
                '}';
    }
}
