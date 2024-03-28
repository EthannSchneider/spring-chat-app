package ch.shkermit.tpi.chatapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.dto.GroupDTO;
import ch.shkermit.tpi.chatapp.dto.MessageDTO;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupNotExistException;
import ch.shkermit.tpi.chatapp.exception.GroupException.GroupOwnerCannotBeRemovedFromTheGroupException;
import ch.shkermit.tpi.chatapp.exception.GroupException.OnlyGroupOwnerCanRemoveMembersException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.Group;
import ch.shkermit.tpi.chatapp.model.GroupContainsMessage;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.GroupMessageProjection;
import ch.shkermit.tpi.chatapp.projection.GroupProjection;
import ch.shkermit.tpi.chatapp.projection.MessageProjection;
import ch.shkermit.tpi.chatapp.projection.OtherUserProjection;
import ch.shkermit.tpi.chatapp.service.GroupService;
import ch.shkermit.tpi.chatapp.service.MessageService;
import ch.shkermit.tpi.chatapp.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/group")
@Tags({ @Tag(name = "Group", description = "Group Controller API") })
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired 
    private MessageService messageService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @SuppressWarnings("null")
    @PostMapping
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Group created successfully"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public ResponseEntity<GroupProjection> createGroup(@RequestBody @Valid GroupDTO groupDTO, @AuthenticationPrincipal UserSession userSession) throws UsersNotExistException, GroupAlreadyExistException {
        Group group = new Group();

        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());

        List<User> users = convertUsernamesToUsers(groupDTO.getMembers());

        group.setMembers(users);
        group.setOwner(userSession.getUserInSession());

        group = groupService.createGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectionFactory.createProjection(GroupProjection.class, group));
    }

    @SuppressWarnings("null")
    @GetMapping
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content)
    })
    public List<GroupProjection> getGroups(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return groupService.getGroupsOfUser(userSession.getUserInSession())
            .stream()
            .map(group -> projectionFactory.createProjection(GroupProjection.class, group))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @GetMapping("/{groupId}/add/{username}")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content)
    })
    public GroupProjection addMemberToGroup(@AuthenticationPrincipal UserSession userSession, @PathVariable String username, @PathVariable String groupId) throws UsersNotExistException, GroupNotExistException {
        User user = userService.getUser(username);
        Group group = groupService.getGroup(groupId);

        ifUserInGroupOrThrowGroupNotExistException(userSession, group);

        if(!group.getMembers().contains(user)) {
            group.getMembers().add(user);

            group = groupService.updateGroup(group);
        }

        return projectionFactory.createProjection(GroupProjection.class, group);
    }

    @SuppressWarnings("null")
    @DeleteMapping("/{groupId}/remove/{username}")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not exist", content = @Content)
    })
    public GroupProjection removeMemberFromGroup(@AuthenticationPrincipal UserSession userSession, @PathVariable String username, @PathVariable String groupId) throws UsersNotExistException, GroupNotExistException, OnlyGroupOwnerCanRemoveMembersException, GroupOwnerCannotBeRemovedFromTheGroupException {
        User user = userService.getUser(username);
        Group group = groupService.getGroup(groupId);

        if(!group.getOwner().equals(userSession.getUserInSession())) {
            throw new OnlyGroupOwnerCanRemoveMembersException();
        }
        if(group.getOwner().equals(user)) {
            throw new GroupOwnerCannotBeRemovedFromTheGroupException();
        }

        group.getMembers().remove(user);

        group = groupService.updateGroup(group);

        return projectionFactory.createProjection(GroupProjection.class, group);
    }

    @GetMapping("/{groupId}/leave")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not exist", content = @Content),
    })
    public ResponseEntity<MessageProjection> leaveGroup(@AuthenticationPrincipal UserSession userSession, @PathVariable String groupId) throws GroupNotExistException, GroupOwnerCannotBeRemovedFromTheGroupException, UsersNotExistException {
        Group group = groupService.getGroup(groupId);

        ifUserInGroupOrThrowGroupNotExistException(userSession, group);

        if(group.getOwner().equals(userSession.getUserInSession())) {
            if (group.getMembers().size() == 0) {
                groupService.deleteGroup(group);
                return ResponseEntity.ok(new MessageProjection("You have left the group and the group has been deleted."));
            }
            group.setOwner(group.getMembers().remove(0));
        }else {
            group.getMembers().remove(userSession.getUserInSession());
        }
        groupService.updateGroup(group);

        return ResponseEntity.ok(new MessageProjection("You have left the group."));
    }

    @SuppressWarnings("null")
    @PostMapping("/{groupId}/message")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not exist", content = @Content),
    })
    public GroupMessageProjection sendMessageInGroup(@AuthenticationPrincipal UserSession userSession, @PathVariable String groupId, @RequestBody MessageDTO messageDTO) throws GroupNotExistException, UsersNotExistException {
        Group group = groupService.getGroup(groupId);

        GroupContainsMessage message = messageService.sendMessageInGroup(messageDTO.getContent(), userSession.getUserInSession(), group);

        return new GroupMessageProjection(
            message.getMessage().getMessageUUID(), 
            message.getMessage().getContent(), 
            projectionFactory.createProjection(OtherUserProjection.class, message.getSender()),
            projectionFactory.createProjection(GroupProjection.class, message.getGroup()), 
            message.getMessage().getSendedAt()
        );
    }

    @SuppressWarnings("null")
    @GetMapping("/{groupId}/messages")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not exist", content = @Content),
    })
    public List<GroupMessageProjection> getMessagesInGroup(@AuthenticationPrincipal UserSession userSession, @PathVariable String groupId, @RequestParam(required = false) Integer page) throws GroupNotExistException {
        Group group = groupService.getGroup(groupId);

        ifUserInGroupOrThrowGroupNotExistException(userSession, group);

        if(page == null) page = 0;

        return messageService.getMessagesFromGroup(group, page)
            .stream()
            .map(message -> new GroupMessageProjection(
                message.getMessage().getMessageUUID(), 
                message.getMessage().getContent(), 
                projectionFactory.createProjection(OtherUserProjection.class, message.getSender()),
                projectionFactory.createProjection(GroupProjection.class, message.getGroup()), 
                message.getMessage().getSendedAt()
            ))
            .toList();
    }

    private List<User> convertUsernamesToUsers(List<String> usernames) throws UsersNotExistException {
        List<User> users = new ArrayList<User>();
        for (String username : usernames) {
            User user = userService.getUser(username);
            users.add(user);
        }
        return users;
    }

    private void ifUserInGroupOrThrowGroupNotExistException(UserSession userSession, Group group) throws GroupNotExistException {
        if(!group.getMembers().contains(userSession.getUserInSession()) && !group.getOwner().equals(userSession.getUserInSession())) {
            throw new GroupNotExistException();
        }
    }
}
