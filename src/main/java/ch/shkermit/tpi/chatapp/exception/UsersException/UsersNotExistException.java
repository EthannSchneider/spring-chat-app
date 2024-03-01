package ch.shkermit.tpi.chatapp.exception.UsersException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User Not Exists")
public class UsersNotExistException extends UsersException {
    
}
