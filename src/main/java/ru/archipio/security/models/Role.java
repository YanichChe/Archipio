package ru.archipio.security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    protected Long id;

    @Column(nullable = false,
            unique = true)
    private String role;


    @ManyToMany(mappedBy = "roleList", fetch = FetchType.EAGER)
    private List<CustomUserDetails> customUserDetails = new ArrayList<>();

    @Override
    public String getAuthority() {
        return role;
    }
}
