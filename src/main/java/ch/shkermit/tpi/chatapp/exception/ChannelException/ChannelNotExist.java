package ch.shkermit.tpi.chatapp.exception.ChannelException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Channel not exist.")
public class ChannelNotExist extends ChannelException {
    
}
