package ch.shkermit.tpi.chatapp.model;

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
public class UserSendMessageToUser {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
	private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(unique = true, nullable = false)
    private Message message;
}