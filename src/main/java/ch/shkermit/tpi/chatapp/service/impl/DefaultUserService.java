package ch.shkermit.tpi.chatapp.service.impl;

import ch.shkermit.tpi.chatapp.exception.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.service.UserService;

public class DefaultUserService implements UserService {

    @Override
    public User getUser(String username) throws UsersNotExistException {
        return null;
    }
    
}
