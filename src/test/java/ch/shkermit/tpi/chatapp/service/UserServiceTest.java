package ch.shkermit.tpi.chatapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

@EnableAutoConfiguration
@SpringBootTest(properties = "spring.profiles.active=test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

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
    public void testGetUserByUsername() throws UsersNotExistException {
        String username = "john.doe";
        User user = userService.getUser(username);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetUserByUsernameAndPassword() throws UsersNotExistException {
        String username = "john.doe";
        String password = "password";
        User user = userService.getUser(username, password);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }

    @Test
    public void testUpdateUser() throws UsersNotExistException {
        String new_first_name = "John1234";
        String new_last_name = "Doe1234";
        String new_password = "newpassword";

        User user = userService.getUser("john.doe");

        user.setFirstName(new_first_name);
        user.setLastName(new_last_name);
        user.setPassword(new_password);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user.getUsername(), updatedUser.getUsername());
        assertEquals(new_first_name, updatedUser.getFirstName());
        assertEquals(new_last_name, updatedUser.getLastName());
        assertTrue(passwordEncoder.matches(new_password, updatedUser.getPassword()));
    }

    @Test
    public void testIsUserExist() {
        String username = "john.doe";
        boolean userExists = userService.isUserExist(username);
        assertTrue(userExists);
    }

    @Test
    public void getUserNotExist() {
        String username = "test-user-not-exist";
        assertThrows(UsersNotExistException.class, () -> userService.getUser(username));
    }

    @Test
    public void createUserAlreadyExist() throws UsersNotExistException {
        User user = userService.getUser("john.doe");

        assertThrows(UsersAlreadyExistException.class, () -> userService.createUser(user));
    }

    @Test
    public void testUpdateUserThatNotExist() {
        User user = new User();
        user.setUsername("test-user-not-exist");
        user.setFirstName("test");
        user.setLastName("user");
        user.setPassword("password");
        user.setEmail("sdoifdjdisf");

        assertThrows(UsersNotExistException.class, () -> userService.updateUser(user));
    }
}
