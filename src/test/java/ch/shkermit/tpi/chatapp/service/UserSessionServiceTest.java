package ch.shkermit.tpi.chatapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import ch.shkermit.tpi.chatapp.exception.SessionException.SessionNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.repository.UserRepository;

@EnableAutoConfiguration
@SpringBootTest(properties = "spring.profiles.active=test")
public class UserSessionServiceTest {

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() throws UsersAlreadyExistException {
        User user = new User();
        user.setUsername("john.doe");
        user.setPassword("password");
        user.setEmail("john.doe@example.com");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPhoneNumber("1234567890");

        userService.createUser(user);
    }

    @SuppressWarnings("null")
    @AfterEach
    public void tearDown() throws UsersNotExistException {
        userRepository.delete(userService.getUser("john.doe"));
    }

    @Test
    public void testGetUserSession() throws UsersNotExistException, SessionNotExistException {
        User user = userService.getUser("john.doe");

        String result = userSessionService.createSession(user);

        String sessionIdInToken = tokenService.decodeToken(result).getPayload().getSubject();

        UserSession userSession = userSessionService.getSession(result);

        assertNotNull(result);
        assertEquals(user.getUsername(), userSession.getUserInSession().getUsername());
        assertEquals(sessionIdInToken, userSession.getSessionUUID());

        userSessionService.deleteSession(userSession);
    }
}