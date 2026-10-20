package projekt.wypozyczalnia.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @ManyToOne
    @JoinColumn(name="Role_id", nullable = false)
    private Role role;

    @Column(name = "Username", length = 50, nullable = false)
    private String username;

    @Column(name = "Password", length = 200, nullable = false)
    private String password;

    @Column(name = "Firstname", length = 40, nullable = false)
    private String firstname;

    @Column(name = "Lastname", length = 80, nullable = false)
    private String lastname;

    @Column(name = "Email", length = 50, nullable = false)
    private String email;

    @Column(name = "Phone", length = 9, nullable = false)
    private String phone;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

