package ru.ccfit.nsu.chernovskaya.Archipio.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import ru.ccfit.nsu.chernovskaya.Archipio.files.models.File;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "title", nullable = false, unique = true)
    @Size(min = 5, max = 50)
    private String title;


    @Column(name = "description")
    @Size(max = 250)
    private String description;

    @OneToMany(mappedBy = "project")
    private List<File> files = new ArrayList<>();

    @Column(name = "likes", nullable = false, columnDefinition = "bigint default 0")
    private long likes;

    @Column(name = "views", nullable = false, columnDefinition = "bigint default 0")
    private long views;

    @Column(name = "visibility", nullable = false, columnDefinition = "boolean default false")
    private boolean visibility;

    @Column(name = "main_image", nullable = false, columnDefinition = "uuid default '21e22474-d31f-4119-8478-d9d448727cfe'")
    private UUID mainImage;

    @ManyToOne
    @Cascade(CascadeType.REMOVE)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", files=" + files +
                ", likes=" + likes +
                ", views=" + views +
                ", visibility=" + visibility +
                ", mainImage=" + mainImage +
                ", owner=" + owner +
                ", tags=" + tags +
                '}';
    }
}
