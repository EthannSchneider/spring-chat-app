package ch.shkermit.tpi.chatapp.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String displayName;
    private String firstName;
	private String lastName;
    private String description;
    private String pronouns;
    private String profilePicture; 
    private String bannerPicture;
}
