package ch.shkermit.tpi.chatapp.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_identity")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
	private Long id;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false, length = 100)
    @ToString.Exclude
    private String password;

    @Column(nullable = false, length = 50)
	private String firstName;
	
	@Column(nullable = false, length = 50)
	private String lastName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 500)
    private String description = "";

    @Column(nullable = false, length=10)
    private String pronouns = "";

    @Column(nullable = false, length=50)
    private String displayName = "";

    @Column
    private String profilePicture;

    @Column
    private String bannerPicture;

    @Column(nullable = false)
    private Date createdAt = new Date();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }
}
