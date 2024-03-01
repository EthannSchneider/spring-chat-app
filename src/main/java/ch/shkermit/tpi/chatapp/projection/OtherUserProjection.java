package ch.shkermit.tpi.chatapp.projection;

public interface OtherUserProjection {
    String getUsername();

    String getDisplayName();

    String getDescription();

    String getProfilePicture();

    String getBannerPicture();

    String getPronouns();
}