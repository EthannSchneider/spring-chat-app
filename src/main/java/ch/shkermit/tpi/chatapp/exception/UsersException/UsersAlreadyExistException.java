package ch.shkermit.tpi.chatapp.exception.UsersException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Already Exists")
public class UsersAlreadyExistException extends UsersException {
    
}