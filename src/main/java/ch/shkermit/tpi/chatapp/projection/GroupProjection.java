package ch.shkermit.tpi.chatapp.projection;

import java.util.List;

public interface GroupProjection {
    String getGroupUUID();

    String getName();
    
    String getDescription();

    List<OtherUserProjection> getMembers();

    OtherUserProjection getOwner();
}
