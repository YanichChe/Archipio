package ru.ccfit.nsu.chernovskaya.Archipio.security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements GrantedAuthority {

    @Id
    protected Long id;

    @Column(nullable = false,
            unique = true)
    private String role;

    @ManyToMany
    @JoinTable(
            name = "UserDetails_Roles",
            joinColumns = { @JoinColumn(name = "role_id") },
            inverseJoinColumns = { @JoinColumn(name = "userDetails_id") }
    )
    private List<CustomUserDetails> customUserDetails;

    @Override
    public String getAuthority() {
        return role;
    }
}
