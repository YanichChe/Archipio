package ru.ccfit.nsu.chernovskaya.Archipio.security.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.ccfit.nsu.chernovskaya.Archipio.user.models.User;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails
        implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(nullable = false)
    private User user;

    @ManyToMany(mappedBy = "customUserDetails")
    @Column(name = "role")
    private List<Roles> rolesList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesList;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
