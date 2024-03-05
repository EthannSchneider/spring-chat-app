package ch.shkermit.tpi.chatapp.repository;

import ch.shkermit.tpi.chatapp.model.Group;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> findByGroupUUID(String groupUUID);
}
