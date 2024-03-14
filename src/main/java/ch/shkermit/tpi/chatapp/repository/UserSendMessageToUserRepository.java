package ch.shkermit.tpi.chatapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ch.shkermit.tpi.chatapp.model.Message;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;

public interface UserSendMessageToUserRepository extends CrudRepository<UserSendMessageToUser, Long>{
    Optional<UserSendMessageToUser> findByMessage(Message message);
}