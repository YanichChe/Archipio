package ru.ccfit.nsu.chernovskaya.Archipio.security.models;

import jakarta.persistence.*;
import lombok.*;
import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private Long id;

    @Column(nullable = false,
            unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private Date expiredTime;
}