package ch.shkermit.tpi.chatapp.service;

import java.util.List;

import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;

public interface UserSessionService {
    public UserSession getSession(String token);

    public List<UserSession> getSession(User user);

    public String createSession(User user);

    public String getSessionToken(UserSession userSession);
}
