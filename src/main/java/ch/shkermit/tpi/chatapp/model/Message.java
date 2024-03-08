package ch.shkermit.tpi.chatapp.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Message {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ToString.Exclude
	private Long id;

    @Column(nullable = false, unique = true)
    private String messageUUID = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String content = "";

    @Column(nullable = false)
    private Date sendedAt = new Date();

    @Column(nullable = false)
    private boolean isDeleted = false;
}
