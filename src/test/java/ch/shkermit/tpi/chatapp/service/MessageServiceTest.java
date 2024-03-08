package ch.shkermit.tpi.chatapp.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import ch.shkermit.tpi.chatapp.exception.GroupException.GroupAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;
import ch.shkermit.tpi.chatapp.repository.GroupContainsMessageRepository;
import ch.shkermit.tpi.chatapp.repository.GroupRepository;
import ch.shkermit.tpi.chatapp.repository.MessageRepository;
import ch.shkermit.tpi.chatapp.repository.UserRepository;
import ch.shkermit.tpi.chatapp.repository.UserSendMessageToUserRepository;

@EnableAutoConfiguration
@SpringBootTest(properties = "spring.profiles.active=test")
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private MessageRepository messageRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserSendMessageToUserRepository userSendMessageToUserRepository;

    @Autowired
    private GroupContainsMessageRepository groupContainsMessageRepository;

    @Autowired
    private GroupService groupService;

    @BeforeEach
    void setUp() throws UsersAlreadyExistException, UsersNotExistException, GroupAlreadyExistException {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUsername("john.doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");
        user1.setPhoneNumber("1234567890");

        user2.setFirstName("John2");
        user2.setLastName("Doe2");
        user2.setUsername("john.doe2");
        user2.setEmail("john.doe2@example.com");
        user2.setPassword("password");
        user2.setPhoneNumber("1234567890");

        user3.setFirstName("John3");
        user3.setLastName("Doe3");
        user3.setUsername("john.doe3");
        user3.setEmail("john.doe3@example.com");
        user3.setPassword("password");
        user3.setPhoneNumber("1234567890");

        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        user3 = userService.createUser(user3);

        Group group = new Group();
        group.setGroupUUID("test");
        group.setName("Test Group");
        group.setDescription("Test Group Description");
        group.setOwner(user1);
        group.getMembers().add(user2);
        group.getMembers().add(user3);

        groupService.createGroup(group);
    }

    @SuppressWarnings("null")
    @AfterEach
    public void tearDown() throws UsersNotExistException, GroupNotExistException {
        clearMessage();

        groupRepository.delete(groupService.getGroup("test"));

        userRepository.delete(userService.getUser("john.doe"));
        userRepository.delete(userService.getUser("john.doe2"));
        userRepository.delete(userService.getUser("john.doe3"));
    }

    @Test
    public void sendMessageInGroupTest() throws UsersNotExistException, GroupNotExistException {
        messageService.sendMessageInGroup("Test Message", userService.getUser("john.doe"), groupService.getGroup("test"));
    }

    @Test
    public void sendMessageToUserTest() throws UsersNotExistException {
        messageService.sendMessageToUser("Test Message", userService.getUser("john.doe"), userService.getUser("john.doe2"));
    }

    @Test
    public void getMessagesFromGroupTest() throws GroupNotExistException, UsersNotExistException {
        messageService.sendMessageInGroup("Test Message", userService.getUser("john.doe"), groupService.getGroup("test"));

        List<GroupContainsMessage> message = messageService.getMessagesFromGroup(groupService.getGroup("test"), 0);

        assert message.size() == 1;
        assert message.get(0).getMessage().getContent().equals("Test Message");
        assert message.get(0).getSender().getUsername().equals("john.doe");

        messageService.sendMessageInGroup("Test Message 2", userService.getUser("john.doe2"), groupService.getGroup("test"));
    }

    @Test
    public void getMessagesFromSenderAndReceiverTest() throws UsersNotExistException {
        messageService.sendMessageToUser("Test Message", userService.getUser("john.doe"), userService.getUser("john.doe2"));

        List<UserSendMessageToUser> message = messageService.getMessagesFromSenderAndReceiver(userService.getUser("john.doe"), userService.getUser("john.doe2"), 0);

        assert message.size() == 1;
        assert message.get(0).getMessage().getContent().equals("Test Message");

        messageService.sendMessageToUser("Test Message 2", userService.getUser("john.doe"), userService.getUser("john.doe2"));

        message = messageService.getMessagesFromSenderAndReceiver(userService.getUser("john.doe2"), userService.getUser("john.doe"), 0);

        assert message.size() == 2;
        assert message.get(0).getMessage().getContent().equals("Test Message 2");
        assert message.get(1).getMessage().getContent().equals("Test Message");
    }

    @SuppressWarnings("null")
    @Test 
    public void sendMessageInGroupFromNotInGroupUserTest() throws UsersNotExistException, GroupNotExistException, UsersAlreadyExistException {
        User user4 = new User();
        user4.setFirstName("John4");
        user4.setLastName("Doe4");
        user4.setUsername("john.doe4");
        user4.setEmail("john.doe4@example.com");
        user4.setPassword("password");
        user4.setPhoneNumber("1234567890");

        userService.createUser(user4);

        User user = userService.getUser("john.doe4");

        assertThrows(GroupNotExistException.class, () -> messageService.sendMessageInGroup("Test Message", user, groupService.getGroup("test")));

        userRepository.delete(user);
    }

    private void clearMessage() {
        userSendMessageToUserRepository.deleteAll();

        groupContainsMessageRepository.deleteAll();

        messageRepository.deleteAll();
    }
}
