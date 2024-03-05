package ch.shkermit.tpi.chatapp.exception.GroupException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Group Not Exist")
public class GroupNotExistException extends GroupException {
    
}
