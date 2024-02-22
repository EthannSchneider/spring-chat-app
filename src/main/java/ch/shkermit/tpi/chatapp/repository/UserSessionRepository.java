package ch.shkermit.tpi.chatapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.model.User;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    Optional<List<UserSession>> findByUserInSession(User userInSession);

    Optional<UserSession> findBySessionUUID(String sessionUUID);
}
