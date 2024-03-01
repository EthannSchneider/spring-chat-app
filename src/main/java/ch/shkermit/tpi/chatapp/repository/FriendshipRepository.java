package ch.shkermit.tpi.chatapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ch.shkermit.tpi.chatapp.model.Friendships;
import ch.shkermit.tpi.chatapp.model.User;

public interface FriendshipRepository extends CrudRepository<Friendships, Long>  {
    Optional<List<Friendships>> findByRequester(User requester);

    Optional<List<Friendships>> findByRequested(User requested);
}
