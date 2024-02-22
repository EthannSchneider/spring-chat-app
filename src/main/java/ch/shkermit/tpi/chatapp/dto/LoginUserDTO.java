package ch.shkermit.tpi.chatapp.dto;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.NonNull;

@Validated
@Data
public class LoginUserDTO {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
