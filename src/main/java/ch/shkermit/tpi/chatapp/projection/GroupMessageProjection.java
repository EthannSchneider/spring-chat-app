package ch.shkermit.tpi.chatapp.projection;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageProjection {
    private String messageUUID;

    private String content;

    private OtherUserProjection sender;

    private GroupProjection group;

    private Date sendedAt;
}
