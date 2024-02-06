package ch.shkermit.tpi.chatapp.security;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

public class CustomJwtDecoder implements JwtDecoder {

    @Override
    public Jwt decode(String token) throws JwtException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "user");
        claims.put("scope", Arrays.asList("read", "write"));
        
        Instant issuedAt = Instant.now().plusSeconds(0); 
        Instant expiresAt = Instant.now().plusSeconds(3600);

        return new Jwt(token, issuedAt, expiresAt, headers, claims);
    }
}
