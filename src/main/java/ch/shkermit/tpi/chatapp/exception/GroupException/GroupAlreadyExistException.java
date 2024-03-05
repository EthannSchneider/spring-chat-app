package ch.shkermit.tpi.chatapp.exception.GroupException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Group already exists")
public class GroupAlreadyExistException extends GroupException {
    
}
