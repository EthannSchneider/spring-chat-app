package ch.shkermit.tpi.chatapp.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenUserProjection {
    private String token;

    private UserProjection user;
}
