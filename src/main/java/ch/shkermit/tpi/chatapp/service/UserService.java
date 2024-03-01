package ch.shkermit.tpi.chatapp.service;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;

public interface UserService {
    public User getUser(String username) throws UsersNotExistException;

    public User getUser(String username, String password) throws UsersNotExistException;

    public User createUser(User user) throws UsersAlreadyExistException;

    public User updateUser(User user) throws UsersNotExistException;

    public boolean isUserExist(String username) throws UsersNotExistException;
}
