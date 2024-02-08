package ch.shkermit.tpi.chatapp.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class User implements UserDetails {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
	private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    @ToString.Exclude
    private String password;

    @Column
	private String name;
	
	@Column
	private String lastName;

    @Column
    private String phoneNumber;

    @Column
    private String description;

    @Column
    private String profilePicture;

    @Column
    private String bannerPicture;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "user");
        return authorities;
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

}
