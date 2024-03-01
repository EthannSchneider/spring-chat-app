package ch.shkermit.tpi.chatapp.service;

import java.util.List;

import ch.shkermit.tpi.chatapp.exception.FriendshipException.CannotBeFriendWithYourselfException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Friendships;
import ch.shkermit.tpi.chatapp.model.User;

public interface FriendshipService {
    Friendships getFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException;

    Friendships addFriendship(User requester, User requested) throws UsersNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException;

    void removeFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException;

    boolean areFriends(User requester, User requested) throws UsersNotExistException;

    Friendships acceptFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException;

    void declineFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException;

    List<Friendships> getAllFriendships(User user) throws UsersNotExistException;

    List<Friendships> getAllFriendships(User user, Friendships.Status status) throws UsersNotExistException;

    List<Friendships> getAllFriendRequests(User user) throws UsersNotExistException;

    List<Friendships> getAllFriendRequestsSent(User user) throws UsersNotExistException;
}
