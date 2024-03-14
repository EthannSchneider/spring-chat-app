package ch.shkermit.tpi.chatapp.repository;

import ch.shkermit.tpi.chatapp.model.Message;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Optional<Message> findByMessageUUID(String messageUUID);
}
