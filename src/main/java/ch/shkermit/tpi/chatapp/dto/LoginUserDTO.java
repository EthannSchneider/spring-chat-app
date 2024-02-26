package ch.shkermit.tpi.chatapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginUserDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
