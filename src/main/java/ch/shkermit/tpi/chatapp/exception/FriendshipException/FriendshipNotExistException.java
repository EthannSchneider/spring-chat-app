package ch.shkermit.tpi.chatapp.exception.FriendshipException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Friendship does not exist")
public class FriendshipNotExistException extends FriendshipException{
    
}
