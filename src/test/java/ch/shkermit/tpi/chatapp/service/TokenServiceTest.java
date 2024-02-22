package ch.shkermit.tpi.chatapp.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration
@SpringBootTest
public class TokenServiceTest {
    @Autowired
    private TokenService tokenService;

    @Test
    public void testCreateToken() {
        String token = tokenService.createToken("testuser");
        assert token != null;
        assert token.length() > 0;
    }

    @Test
    public void testValidateToken() {
        String user = "testuser";
        String token = tokenService.createToken(user);
        assert tokenService.decodeToken(token).getPayload().getSubject().equals(user);
    }

    @Test
    public void testFakeToken() {
        assertThrows(Exception.class, () -> tokenService.decodeToken("hello there!"));
    }
}
