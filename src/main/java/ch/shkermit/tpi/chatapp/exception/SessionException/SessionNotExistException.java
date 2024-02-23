package ch.shkermit.tpi.chatapp.exception.SessionException;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Session Not Exists")
public class SessionNotExistException extends SessionException{
    
}
