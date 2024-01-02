package ru.archipio.files.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import ru.archipio.project.models.Project;

import java.util.UUID;

@Entity(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @Cascade(CascadeType.REMOVE)
    @JoinColumn(name = "project_id")
    private Project project;

    public File(String name) {
        this.name = name;
    }
}
