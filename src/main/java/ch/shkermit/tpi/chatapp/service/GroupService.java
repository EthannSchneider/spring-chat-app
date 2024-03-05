package ch.shkermit.tpi.chatapp.service;

import java.util.List;

import ch.shkermit.tpi.chatapp.exception.GroupException.GroupAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.User;

public interface GroupService {
    public Group getGroup(String groupUUID) throws GroupNotExistException;

    public Group createGroup(Group newGroup) throws UsersNotExistException, GroupAlreadyExistException;

    public List<Group> getGroupsOfUser(User user) throws UsersNotExistException;

    public Group updateGroup(Group group) throws GroupNotExistException, UsersNotExistException;

    public Group deleteGroup(Group group) throws GroupNotExistException;
}
