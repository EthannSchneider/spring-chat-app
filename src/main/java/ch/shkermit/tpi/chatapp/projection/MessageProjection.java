package ch.shkermit.tpi.chatapp.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageProjection {
    private String message;
}