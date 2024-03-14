package ch.shkermit.tpi.chatapp.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;

public interface GroupContainsMessageSortingRepository extends PagingAndSortingRepository<GroupContainsMessage, Long> {
    List<GroupContainsMessage> findAllByGroupOrderByIdDesc(Group group, Pageable pageable);    
}
