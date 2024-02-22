package ch.shkermit.tpi.chatapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ch.shkermit.tpi.chatapp.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
