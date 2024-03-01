package ch.shkermit.tpi.chatapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.exception.FriendshipException.CannotBeFriendWithYourselfException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.FriendshipException.FriendshipNotExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Friendships;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.FriendProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.FriendshipService;
import ch.shkermit.tpi.chatapp.service.UserService;

@RestController
@RequestMapping("api/user")
public class FriendController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ProjectionFactory projectionFactory;
    
    @GetMapping("friends")
    public List<FriendProjection> getFriends(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendships(userSession.getUserInSession(), Friendships.Status.ACCEPTED), userSession.getUserInSession());
    }

    @GetMapping("friends/requests")
    public List<FriendProjection> getFriendsRequest(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendRequests(userSession.getUserInSession()), userSession.getUserInSession());
    }

    @GetMapping("friends/requests/sent")
    public List<FriendProjection> getSentFriendsRequest(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendRequestsSent(userSession.getUserInSession()), userSession.getUserInSession());
    }

    @GetMapping("{username}/friends")
    public ResponseEntity<FriendProjection> addFriend(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        Friendships friendships = friendshipService.addFriendship(current_user, new_friend);

        return ResponseEntity.ok().body(getFriendProjection(friendships, current_user));
    }

    @GetMapping("{username}/friends/accept")
    public FriendProjection acceptFriendRequest(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        return getFriendProjection(friendshipService.acceptFriendship(current_user, new_friend), current_user);
    }

    @GetMapping("{username}/friends/decline")
    public String declineFriendRequest(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        friendshipService.declineFriendship(current_user, new_friend);

        return "{\"message\": \"Friend request declined\"}";
    }

    @GetMapping("{username}/friends/remove")
    public String removeFriend(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        friendshipService.removeFriendship(current_user, new_friend);

        return "{\"message\": \"Friend removed\"}";
    }

    private List<FriendProjection> getFriendsProjections(List<Friendships> friendships, User currentUser) {
        List<FriendProjection> friends = new ArrayList<>();

        for (Friendships friendship : friendships) {
            friends.add(getFriendProjection(friendship, currentUser));
        }
        return friends;
    }

    @SuppressWarnings("null")
    private FriendProjection getFriendProjection(Friendships friendships, User currentUser) {
        User user = friendships.getRequester().equals(currentUser) ? friendships.getRequested() : friendships.getRequester();
        return new FriendProjection(projectionFactory.createProjection(UserProjection.class, user), friendships.getStatus().toString());
    }
}
