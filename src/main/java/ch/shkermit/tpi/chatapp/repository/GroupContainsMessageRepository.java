package ch.shkermit.tpi.chatapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;
import ch.shkermit.tpi.chatapp.model.Message;

public interface GroupContainsMessageRepository extends CrudRepository<GroupContainsMessage, Long>{
    Optional<GroupContainsMessage> findByMessage(Message message);
}