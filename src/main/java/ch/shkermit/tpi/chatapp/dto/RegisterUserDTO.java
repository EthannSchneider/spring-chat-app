package ch.shkermit.tpi.chatapp.dto;

import ch.shkermit.tpi.chatapp.utils.RegexUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    @Pattern(regexp = RegexUtils.email)
    private String email;

    @NotEmpty
    @Pattern(regexp = RegexUtils.phonenumber)
    private String phoneNumber;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String lastName;

    @NotEmpty
    private String password;
}
