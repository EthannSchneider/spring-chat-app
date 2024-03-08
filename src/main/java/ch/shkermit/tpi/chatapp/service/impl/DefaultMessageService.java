package ch.shkermit.tpi.chatapp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;
import ch.shkermit.tpi.chatapp.model.Message;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;
import ch.shkermit.tpi.chatapp.repository.GroupContainsMessageRepository;
import ch.shkermit.tpi.chatapp.repository.GroupContainsMessageSortingRepository;
import ch.shkermit.tpi.chatapp.repository.MessageRepository;
import ch.shkermit.tpi.chatapp.repository.UserSendMessageToUserRepository;
import ch.shkermit.tpi.chatapp.repository.UserSendMessageToUserSortingRepository;
import ch.shkermit.tpi.chatapp.service.GroupService;
import ch.shkermit.tpi.chatapp.service.MessageService;
import ch.shkermit.tpi.chatapp.service.UserService;

public class DefaultMessageService implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserSendMessageToUserRepository userSendMessageToUserRepository; 

    @Autowired
    private GroupContainsMessageRepository groupContainsMessageRepository; 

    @Autowired
    private GroupContainsMessageSortingRepository groupContainsMessageSortingRepository;

    @Autowired
    private UserSendMessageToUserSortingRepository userSendMessageToUserSortingRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Override
    public GroupContainsMessage sendMessageInGroup(String message, User sender, Group group) throws GroupNotExistException, UsersNotExistException {
        isGroupExistOrThrowException(group);
        isUserExistOrThrowException(sender);

        if (!group.getMembers().contains(sender) && !group.getOwner().equals(sender)) {
            throw new GroupNotExistException();
        }

        Message newMessage = new Message();
        newMessage.setContent(message);

        newMessage = messageRepository.save(newMessage);

        GroupContainsMessage groupContainsMessage = new GroupContainsMessage();
        groupContainsMessage.setMessage(newMessage);
        groupContainsMessage.setSender(sender);
        groupContainsMessage.setGroup(group);

        return groupContainsMessageRepository.save(groupContainsMessage);
    }

    @Override
    public UserSendMessageToUser sendMessageToUser(String message, User sender, User receiver) throws UsersNotExistException {
        isUserExistOrThrowException(sender, receiver);

        Message newMessage = new Message();
        newMessage.setContent(message);

        newMessage = messageRepository.save(newMessage);

        UserSendMessageToUser userSendMessageToUser = new UserSendMessageToUser();
        userSendMessageToUser.setMessage(newMessage);
        userSendMessageToUser.setSender(sender);
        userSendMessageToUser.setReceiver(receiver);

        return userSendMessageToUserRepository.save(userSendMessageToUser);
    }

    @Override
    public List<GroupContainsMessage> getMessagesFromGroup(Group group, Integer page) throws GroupNotExistException {
        isGroupExistOrThrowException(group);
        return groupContainsMessageSortingRepository.findAllByGroupOrderByIdDesc(group, Pageable.ofSize(20).withPage(page));
    }

    @Override
    public List<UserSendMessageToUser> getMessagesFromSenderAndReceiver(User sender, User receiver, Integer page) throws UsersNotExistException {
        isUserExistOrThrowException(sender, receiver);
        List<UserSendMessageToUser> conversation1 = userSendMessageToUserSortingRepository.findAllBySenderAndReceiverOrderByIdDesc(sender, receiver, Pageable.ofSize(20).withPage(page));
        List<UserSendMessageToUser> conversation2 = userSendMessageToUserSortingRepository.findAllBySenderAndReceiverOrderByIdDesc(receiver, sender, Pageable.ofSize(20).withPage(page));
        List<UserSendMessageToUser> mergedConversation = new ArrayList<>();
        mergedConversation.addAll(conversation1);
        mergedConversation.addAll(conversation2);
        Collections.sort(mergedConversation, Comparator.comparing(UserSendMessageToUser::getId).reversed());

        if (mergedConversation.size() > 20) {
            mergedConversation = mergedConversation.subList(0, 20);
        }

        return mergedConversation;
    }

    private void isUserExistOrThrowException(User... user) throws UsersNotExistException {
        for(User u : user) {
            if(!userService.isUserExist(u.getUsername())) {
                throw new UsersNotExistException();
            }
        }
    }

    private void isGroupExistOrThrowException(Group group) throws GroupNotExistException {
        if(!groupService.isGroupExist(group.getGroupUUID())) {
            throw new GroupNotExistException();
        }
    }
}
