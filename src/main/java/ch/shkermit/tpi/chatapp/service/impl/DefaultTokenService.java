package ch.shkermit.tpi.chatapp.service.impl;

import ch.shkermit.tpi.chatapp.service.TokenService;
import ch.shkermit.tpi.chatapp.utils.SecretGeneratorUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class DefaultTokenService implements TokenService {
    @Override
    public String createToken(String userSessionId) {
        return Jwts.builder().subject(userSessionId).signWith(SecretGeneratorUtils.generateSecretKey()).compact();   
    }

    @Override
    public Jws<Claims> decodeToken(String token) {
        return Jwts.parser().verifyWith(SecretGeneratorUtils.generateSecretKey()).build().parseSignedClaims(token);
    }
}
