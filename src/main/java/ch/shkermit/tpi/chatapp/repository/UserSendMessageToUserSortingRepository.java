package ch.shkermit.tpi.chatapp.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;

import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;

public interface UserSendMessageToUserSortingRepository extends PagingAndSortingRepository<UserSendMessageToUser, Long> {
    List<UserSendMessageToUser> findAllBySenderAndReceiverOrderByIdDesc(User sender, User receiver, Pageable pageable);
}
