package ch.shkermit.tpi.chatapp.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDTO {

    private String displayName;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
	private String lastName;

    private String description;

    private String pronouns;

    private String profilePicture; 
    
    private String bannerPicture;
}
