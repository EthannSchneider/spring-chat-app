package ch.shkermit.tpi.chatapp.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import ch.shkermit.tpi.chatapp.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;

public class CustomJwtDecoder implements JwtDecoder {

    @Autowired
    private TokenService tokenService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {        
            Jws<Claims> jwsToken = tokenService.decodeToken(token.split(" ")[1]);

            JwsHeader headers = jwsToken.getHeader();

            Claims claims = jwsToken.getPayload();

            Instant issuedAt = new Date().toInstant();

            Instant expiresAt = new Date().toInstant().plusSeconds(100);

            return new Jwt(token, issuedAt, expiresAt, headers, claims);

        } catch (Exception e) {
            throw new JwtException("Authentication Exception - Invalid Token");
        }
    }
}
