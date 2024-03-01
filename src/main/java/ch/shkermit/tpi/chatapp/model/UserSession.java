package ch.shkermit.tpi.chatapp.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class UserSession {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
    private Long id;

    @Column(nullable = false, unique = true)
    private String sessionUUID = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(nullable = false)
    private User userInSession;

    @Column(nullable = false)
    private Date createdAt = new Date();
}
