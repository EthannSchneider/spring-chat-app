package ch.shkermit.tpi.chatapp.service;

import java.util.ArrayList;
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
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.repository.UserRepository;

@EnableAutoConfiguration
@SpringBootTest(properties = "spring.profiles.active=test")
public class GroupServiceTest {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws UsersAlreadyExistException {
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

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
    }

    @SuppressWarnings("null")
    @AfterEach
    public void tearDown() throws UsersNotExistException {
        userRepository.delete(userService.getUser("john.doe"));
        userRepository.delete(userService.getUser("john.doe2"));
        userRepository.delete(userService.getUser("john.doe3"));
    }
    
    @Test
    public void testCreateGroup() throws UsersNotExistException, GroupAlreadyExistException, GroupNotExistException {
        Group group = new Group();

        List<User> members = new ArrayList<User>();
        members.add(userService.getUser("john.doe2"));

        group.setName("Test Group");
        group.setDescription("This is a test group");
        group.setMembers(members);
        group.setOwner(userService.getUser("john.doe"));

        groupService.createGroup(group);

        groupService.deleteGroup(group);
    }

    @Test
    public void testUpdateGroup() throws UsersNotExistException, GroupAlreadyExistException, GroupNotExistException {
        Group group = new Group();

        List<User> members = new ArrayList<User>();
        members.add(userService.getUser("john.doe2"));

        group.setName("Test Group");
        group.setDescription("This is a test group");
        group.setMembers(members);
        group.setOwner(userService.getUser("john.doe"));

        groupService.createGroup(group);

        group.setName("Test Group 2");

        members.add(userService.getUser("john.doe3"));
        group.setMembers(members);
        group.setDescription("This is a test group 2");

        groupService.updateGroup(group);

        groupService.deleteGroup(group);
    }

    @Test
    public void testDeleteGroup() throws UsersNotExistException, GroupAlreadyExistException, GroupNotExistException {
        Group group = new Group();

        List<User> members = new ArrayList<User>();
        members.add(userService.getUser("john.doe2"));

        group.setName("Test Group");
        group.setDescription("This is a test group");
        group.setMembers(members);
        group.setOwner(userService.getUser("john.doe"));

        groupService.createGroup(group);

        groupService.deleteGroup(group);
    }
}
