package ru.ccfit.nsu.chernovskaya.Archipio.user.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ccfit.nsu.chernovskaya.Archipio.project.models.Project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false,
            unique = true)
    private String login;

    @Column(unique = true,
            nullable = false)
    private String email;

    @Column
    private Date createdAt;

    @Column(unique = true, columnDefinition = "varchar(256) default '/home/downloads/apple.png'")
    private String profilePic;

    @OneToMany(mappedBy = "owner")
    private List<Project> projects = new ArrayList<>();
}