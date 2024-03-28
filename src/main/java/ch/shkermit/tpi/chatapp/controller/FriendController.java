package ch.shkermit.tpi.chatapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ch.shkermit.tpi.chatapp.projection.MessageProjection;
import ch.shkermit.tpi.chatapp.projection.OtherUserProjection;
import ch.shkermit.tpi.chatapp.service.FriendshipService;
import ch.shkermit.tpi.chatapp.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

@RestController
@RequestMapping("api/user")
@Tags({ @Tag(name = "Friends", description = "Friends Controller API") })
public class FriendController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ProjectionFactory projectionFactory;
    
    @GetMapping("friends")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public List<FriendProjection> getFriends(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendships(userSession.getUserInSession(), Friendships.Status.ACCEPTED), userSession.getUserInSession());
    }

    @GetMapping("friends/requests")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public List<FriendProjection> getFriendsRequest(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendRequests(userSession.getUserInSession()), userSession.getUserInSession());
    }

    @GetMapping("friends/requests/sent")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public List<FriendProjection> getSentFriendsRequest(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return getFriendsProjections(friendshipService.getAllFriendRequestsSent(userSession.getUserInSession()), userSession.getUserInSession());
    }

    @GetMapping("{username}/friends")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Friend request sent"),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content),
        @ApiResponse(responseCode = "400", description = "Already friend", content = @Content),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public ResponseEntity<FriendProjection> addFriend(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException, FriendshipAlreadyExistException, CannotBeFriendWithYourselfException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        Friendships friendships = friendshipService.addFriendship(current_user, new_friend);

        return ResponseEntity.ok().body(getFriendProjection(friendships, current_user));
    }

    @GetMapping("{username}/friends/accept")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Accepted friend request"),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Friendship not exist", content = @Content),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public FriendProjection acceptFriendRequest(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        return getFriendProjection(friendshipService.acceptFriendship(current_user, new_friend), current_user);
    }

    @GetMapping("{username}/friends/decline")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Declined friend request"),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Friendship not exist", content = @Content),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public MessageProjection declineFriendRequest(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        friendshipService.declineFriendship(current_user, new_friend);

        return new MessageProjection("Friend request declined");
    }

    @DeleteMapping("{username}/friends/remove")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Friend removed"),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Friendship not exist", content = @Content),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public MessageProjection removeFriend(@AuthenticationPrincipal UserSession userSession, @PathVariable String username) throws UsersNotExistException, FriendshipNotExistException {
        User current_user = userSession.getUserInSession();
        User new_friend = userService.getUser(username);

        friendshipService.removeFriendship(current_user, new_friend);

        return new MessageProjection("Friend removed");
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
        return new FriendProjection(projectionFactory.createProjection(OtherUserProjection.class, user), friendships.getStatus().toString());
    }
}
