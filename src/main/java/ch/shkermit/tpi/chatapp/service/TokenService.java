package ch.shkermit.tpi.chatapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenService {
    public String createToken(String userSessionId);

    public Jws<Claims> decodeToken(String token);
}
