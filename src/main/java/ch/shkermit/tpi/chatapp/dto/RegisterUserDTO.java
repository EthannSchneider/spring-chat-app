package ch.shkermit.tpi.chatapp.dto;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.NonNull;

@Validated
@Data
public class RegisterUserDTO {
    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String phoneNumber;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String password;
}
