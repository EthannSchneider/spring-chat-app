package ch.shkermit.tpi.chatapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.shkermit.tpi.chatapp.exception.SessionException.SessionNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.repository.UserSessionRepository;
import ch.shkermit.tpi.chatapp.service.TokenService;
import ch.shkermit.tpi.chatapp.service.UserSessionService;

public class DefaultUserSessionService implements UserSessionService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public UserSession getSession(String token) throws SessionNotExistException {
        return userSessionRepository.findBySessionUUID(tokenService.decodeToken(token).getPayload().getSubject()).orElseThrow(SessionNotExistException::new);
    }

    @Override
    public List<UserSession> getSession(User user) {
        return userSessionRepository.findByUserInSession(user).orElse(new ArrayList<>());
    }

    @Override
    public String createSession(User user) {
        UserSession newSession = new UserSession();
        newSession.setUserInSession(user);
        userSessionRepository.save(newSession);
        return tokenService.createToken(newSession.getSessionUUID());
    }

    @Override
    public String getSessionToken(UserSession userSession) throws SessionNotExistException {
        isSessionExist(userSession);
        return tokenService.createToken(userSession.getSessionUUID());
    }

    @SuppressWarnings("null")
    @Override
    public void deleteSession(UserSession userSession) throws SessionNotExistException {
        isSessionExist(userSession);
        userSessionRepository.delete(userSession);
    }

    private void isSessionExist(UserSession userSession) throws SessionNotExistException {
        if (userSessionRepository.findBySessionUUID(userSession.getSessionUUID()).isEmpty()) {
            throw new SessionNotExistException();
        }
    }
}
