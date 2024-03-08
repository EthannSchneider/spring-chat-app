package ch.shkermit.tpi.chatapp.service;

import java.util.List;

import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;

public interface MessageService {
    public GroupContainsMessage sendMessageInGroup(String message, User sender, Group group) throws GroupNotExistException, UsersNotExistException;

    public UserSendMessageToUser sendMessageToUser(String message, User sender, User receiver) throws UsersNotExistException;
    
    public List<GroupContainsMessage> getMessagesFromGroup(Group group, Integer page) throws GroupNotExistException;

    public List<UserSendMessageToUser> getMessagesFromSenderAndReceiver(User sender, User receiver, Integer page) throws UsersNotExistException;
}