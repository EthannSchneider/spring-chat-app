package ch.shkermit.tpi.chatapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.shkermit.tpi.chatapp.exception.GroupException.GroupAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.repository.GroupRepository;
import ch.shkermit.tpi.chatapp.service.GroupService;
import ch.shkermit.tpi.chatapp.service.UserService;

public class DefaultGroupService implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    public Group getGroup(String groupUUID) throws GroupNotExistException {
        return groupRepository.findByGroupUUID(groupUUID).orElseThrow(GroupNotExistException::new);
    }

    @Override
    public Group createGroup(Group newGroup) throws GroupAlreadyExistException, UsersNotExistException {
        if (groupRepository.findByGroupUUID(newGroup.getGroupUUID()).isPresent()) {
            throw new GroupAlreadyExistException();
        }

        checkUserExistence(newGroup);

        return groupRepository.save(newGroup);
    }

    @Override
    public List<Group> getGroupsOfUser(User user) {
        List<Group> groups = new ArrayList<>();
        groupRepository.findAll().forEach(group -> {
            if (group.getMembers().contains(user)) {
                groups.add(group);
            }else if (group.getOwner().equals(user)) {
                groups.add(group);
            }
        });

        return groups;
    }

    @Override
    public Group updateGroup(Group group) throws GroupNotExistException, UsersNotExistException {
        if (groupRepository.findByGroupUUID(group.getGroupUUID()).isEmpty()) {
            throw new GroupNotExistException();
        }

        checkUserExistence(group);

        return groupRepository.save(group);
    }

    @Override
    public Group deleteGroup(Group group) throws GroupNotExistException {
        if (groupRepository.findByGroupUUID(group.getGroupUUID()).isEmpty()) {
            throw new GroupNotExistException();
        }

        groupRepository.delete(group);

        return group;
    }

    @Override
    public boolean isGroupExist(String groupUUID) {
        return groupRepository.findByGroupUUID(groupUUID).isPresent();
    }

    private void checkUserExistence(Group group) throws UsersNotExistException {
        for(User user : group.getMembers()) {
            if (!userService.isUserExist(user.getUsername())) {
                throw new UsersNotExistException();
            }
        }
        if (!userService.isUserExist(group.getOwner().getUsername())) {
            throw new UsersNotExistException();
        }
    }
}
