package ch.shkermit.tpi.chatapp.exception.GroupException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Only group owner can remove members from the group.")
public class OnlyGroupOwnerCanRemoveMembersException extends GroupException {
    
}
