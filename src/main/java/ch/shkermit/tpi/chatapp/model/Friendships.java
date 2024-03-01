package ch.shkermit.tpi.chatapp.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
@Entity
public class Friendships {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
	private Long id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User requested;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Date createdAt = new Date();

    public enum Status {
        PENDING,
        ACCEPTED
    }
}
