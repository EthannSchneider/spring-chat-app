package ch.shkermit.tpi.chatapp.service;

import ch.shkermit.tpi.chatapp.exception.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;

public interface UserService {
    User getUser(String username) throws UsersNotExistException;
}
