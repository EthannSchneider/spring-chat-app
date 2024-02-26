package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class FriendController {
    @GetMapping("friends")
    public String getFriends() {
        return "Hello from get friends controller";
    }

    @PostMapping("{username}/friends")
    public String addFriend() {
        return "Hello from add friend controller";
    }

    @PostMapping("{username}/friends/accept")
    public String acceptFriendRequest() {
        return "Hello from accept friend request controller";
    }

    @PostMapping("{username}/friends/decline")
    public String declineFriendRequest() {
        return "Hello from decline friend request controller";
    }

    @DeleteMapping("{username}/friends")
    public String removeFriend() {
        return "Hello from remove friend controller";
    }    
}
