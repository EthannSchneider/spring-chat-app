package ch.shkermit.tpi.chatapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import ch.shkermit.tpi.chatapp.exception.FriendshipException.CannotBeFriendWithYourselfException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Friendships;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.repository.FriendshipRepository;
import ch.shkermit.tpi.chatapp.service.FriendshipService;
import ch.shkermit.tpi.chatapp.service.UserService;

public class DefaultFriendshipService implements FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired 
    private UserService userService;

    @Override
    public Friendships getFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException {
        try {
            return getFriendshipInOrder(requester, requested);
        } catch(FriendshipNotExistException e) {
            return getFriendshipInOrder(requested, requester);
        }
    }

    @Override
    public Friendships addFriendship(User requester, User requested) throws UsersNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        if(requester.equals(requested)) {
            throw new CannotBeFriendWithYourselfException();
        }
        if(areFriends(requester, requested)) {
            throw new FriendshipAlreadyExistException();
        }
        Friendships friendship = new Friendships();
        friendship.setRequester(requester);
        friendship.setRequested(requested);
        friendship.setStatus(Friendships.Status.PENDING);

        return friendshipRepository.save(friendship);
    }

    @SuppressWarnings("null")
    @Override
    public void removeFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException {
        Friendships friendship = getFriendship(requester, requested);

        friendshipRepository.delete(friendship);
    }

    @Override
    public boolean areFriends(User requester, User requested) throws UsersNotExistException {
        try {
            getFriendship(requester, requested);
            return true;
        } catch(FriendshipNotExistException e) {
            return false;
        }
    }

    @Override
    public Friendships acceptFriendship(User requester, User requested) throws UsersNotExistException, FriendshipNotExistException {
        Friendships friendship = getFriendshipInOrder(requested, requester);

        if(!friendship.getStatus().equals(Friendships.Status.PENDING)) {
            throw new FriendshipNotExistException();
        }

        friendship.setStatus(Friendships.Status.ACCEPTED);

        return friendshipRepository.save(friendship);
    }

    @Override
    public void declineFriendship(User requester, User requested) throws FriendshipNotExistException, UsersNotExistException {
        Friendships friendship = getFriendshipInOrder(requested, requester);

        if(!friendship.getStatus().equals(Friendships.Status.PENDING)) {
            throw new FriendshipNotExistException();
        }

        removeFriendship(requester, requested);
    }

    @Override
    public List<Friendships> getAllFriendships(User user) throws UsersNotExistException {
        isUserExistOrThrowException(user);
        List<Friendships> friendships = friendshipRepository.findByRequester(user).orElse(new ArrayList<>());
        for(Friendships friendship : friendshipRepository.findByRequested(user).orElse(new ArrayList<>())) {
            if(!friendships.contains(friendship)) {
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    @Override
    public List<Friendships> getAllFriendships(User user, Friendships.Status status) throws UsersNotExistException {
        List<Friendships> acceptedFriendships = new ArrayList<>();
        for(Friendships friendship : getAllFriendships(user)) {
            if(friendship.getStatus() == status) {
                acceptedFriendships.add(friendship);
            }
        }
        return acceptedFriendships;
    }

    @Override
    public List<Friendships> getAllFriendRequests(User user) throws UsersNotExistException {
        return getFriendRequest(user, true);
    }

    @Override
    public List<Friendships> getAllFriendRequestsSent(User user) throws UsersNotExistException {
        return getFriendRequest(user, false);
    }

    private List<Friendships> getFriendRequest(User user, boolean received) throws UsersNotExistException {
        isUserExistOrThrowException(user);
        List<Friendships> friendRequestsSent = new ArrayList<>();
        Optional<List<Friendships>> friendships = received ? 
            friendshipRepository.findByRequested(user) :
            friendshipRepository.findByRequester(user);
        for(Friendships friendship : friendships.orElse(new ArrayList<>())) {
            if(friendship.getStatus() == Friendships.Status.PENDING) {
                friendRequestsSent.add(friendship);
            }
        }
        return friendRequestsSent;
    }

    private void isUserExistOrThrowException(User... users) throws UsersNotExistException {
        for (User user : users) {
            if(!userService.isUserExist(user.getUsername())) {
                throw new UsersNotExistException();
           }
        }
    }

    private Friendships getFriendshipInOrder(User requester, User requested) throws FriendshipNotExistException, UsersNotExistException {
        isUserExistOrThrowException(requester, requested);
        return friendshipRepository.findByRequester(requester).orElse(new ArrayList<>()).stream()
            .filter(f -> f.getRequested().equals(requested))
            .findFirst().orElseThrow(FriendshipNotExistException::new);
    }
}
