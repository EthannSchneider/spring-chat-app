package ch.shkermit.tpi.chatapp.exception.FriendshipException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cannot be friend with yourself")
public class CannotBeFriendWithYourselfException extends FriendshipException{
    
}
