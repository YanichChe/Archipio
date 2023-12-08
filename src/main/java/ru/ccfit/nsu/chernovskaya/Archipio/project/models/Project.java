package ru.ccfit.nsu.chernovskaya.Archipio.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "main_image", nullable = false, columnDefinition = "varchar(90) default 'defaultImage'")
    private String mainImage;

    @ManyToOne
    @Cascade(CascadeType.REMOVE)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();
}
