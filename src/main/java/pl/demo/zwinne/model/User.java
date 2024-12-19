package pl.demo.zwinne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ADM_USER")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 100)
    private String surname;

    @Column(name = "EMAIL", nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "INDEX_NUMBER", nullable = false, length = 20)
    private String indexNumber;

    @Column(name = "STATIONARY", nullable = false)
    private boolean stationary;

    @CreationTimestamp
    @Column(updatable = false, name = "CREATED_AT")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());

        return List.of(authority);
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

    public User(String name,String surname, String email, String password, String indexNumber, boolean stationary, Role role) {
        this.stationary = stationary;
        this.indexNumber = indexNumber;
        this.password = password;
        this.email = email;
        this.surname = surname;
        this.name = name;
        this.role = role;
    }
}
