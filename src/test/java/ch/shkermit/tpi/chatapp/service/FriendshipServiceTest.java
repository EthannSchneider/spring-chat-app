package ch.shkermit.tpi.chatapp.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import ch.shkermit.tpi.chatapp.exception.FriendshipException.CannotBeFriendWithYourselfException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Friendships;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.Friendships.Status;
import ch.shkermit.tpi.chatapp.repository.UserRepository;

@EnableAutoConfiguration
@SpringBootTest(properties = "spring.profiles.active=test")
public class FriendshipServiceTest {
    @Autowired
    private FriendshipService friendshipService;

    @Autowired 
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws UsersAlreadyExistException {
        User requester = new User();
        User requested = new User();

        requester.setFirstName("John");
        requester.setLastName("Doe");
        requester.setUsername("john.doe");
        requester.setEmail("john.doe@example.com");
        requester.setPassword("password");
        requester.setPhoneNumber("1234567890");

        requested.setFirstName("John2");
        requested.setLastName("Doe2");
        requested.setUsername("john.doe2");
        requested.setEmail("john.doe2@example.com");
        requested.setPassword("password");
        requested.setPhoneNumber("1234567890");

        userService.createUser(requester);
        userService.createUser(requested);
    }

    @SuppressWarnings("null")
    @AfterEach
    public void tearDown() throws UsersNotExistException {
        userRepository.delete(userService.getUser("john.doe"));
        userRepository.delete(userService.getUser("john.doe2"));
    }

    @Test
    void testAddGetFriendship() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Act
        Friendships created = friendshipService.addFriendship(requester, requested);
        Friendships found = friendshipService.getFriendship(requester, requested);

        // Assert
        assertNotNull(created);
        assertEquals(created, found);

        // Act
        friendshipService.removeFriendship(requester, requested);
    }

    @Test
    void testAddFriendshipAlreadyExist() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Act
        friendshipService.addFriendship(requester, requested);

        // Assert
        assertThrows(FriendshipAlreadyExistException.class, () -> friendshipService.addFriendship(requester, requested));

        // Act
        friendshipService.removeFriendship(requester, requested);
    }

    @Test
    void testAcceptFriendship() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Act
        Friendships created = friendshipService.addFriendship(requester, requested);
        assertTrue(created.getStatus().equals(Status.PENDING));

        Friendships accepted = friendshipService.acceptFriendship(requested, requester);

        // Assert
        assertNotNull(created);
        assertNotNull(accepted);
        assertTrue(accepted.getStatus().equals(Status.ACCEPTED));
        assertThrows(FriendshipNotExistException.class, () -> friendshipService.declineFriendship(requester, requested));

        // Act
        friendshipService.removeFriendship(requester, requested);
    }

    @Test
    void testAcceptFriendshipNotExist() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Assert
        assertThrows(FriendshipNotExistException.class, () -> friendshipService.acceptFriendship(requester, requested));
    }

    @Test
    void testDelineFriendship() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Act
        Friendships created = friendshipService.addFriendship(requester, requested);
        assertTrue(created.getStatus().equals(Status.PENDING));

        friendshipService.declineFriendship(requested, requester);

        // Assert
        assertNotNull(created);
        assertThrows(FriendshipNotExistException.class, () -> friendshipService.removeFriendship(requester, requested));
    }

    @Test
    void testDelineFriendshipNotExist() throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        // Arrange
        User requester = userService.getUser("john.doe");
        User requested = userService.getUser("john.doe2");

        // Assert
        assertThrows(FriendshipNotExistException.class, () -> friendshipService.declineFriendship(requester, requested));
    }

}
