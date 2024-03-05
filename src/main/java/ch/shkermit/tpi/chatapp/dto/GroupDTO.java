package ch.shkermit.tpi.chatapp.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GroupDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private List<String> members = new ArrayList<String>();
}
