package ch.shkermit.tpi.chatapp.projection;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserToUserMessageProjection {
    private String messageUUID;

    private String content;

    private OtherUserProjection sender;

    private OtherUserProjection receiver;

    private Date sendedAt;
}