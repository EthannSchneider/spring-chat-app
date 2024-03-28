package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.dto.MessageDTO;
import ch.shkermit.tpi.chatapp.dto.UpdateUserDTO;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSendMessageToUser;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.OtherUserProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.projection.UserToUserMessageProjection;
import ch.shkermit.tpi.chatapp.service.MessageService;
import ch.shkermit.tpi.chatapp.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SuppressWarnings("null")
@RestController
@RequestMapping("api/user")
@Tags({ @Tag(name = "User", description = "User Controller API") })
public class UserController {
    @Autowired 
    private ProjectionFactory projectionFactory;

    @Autowired
    private UserService userService;

    @Autowired 
    private MessageService messageService;
    
    @GetMapping
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
    })
    public UserProjection getUser(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return projectionFactory.createProjection(UserProjection.class, userSession.getUserInSession());
    }

    @GetMapping("{username}")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
    })
    public OtherUserProjection getUserByUsername(@PathVariable String username) throws UsersNotExistException {
        return projectionFactory.createProjection(OtherUserProjection.class, userService.getUser(username));
    }

    @PutMapping
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
    })
    public UserProjection updateUser(@AuthenticationPrincipal UserSession userSession, @RequestBody UpdateUserDTO userUpdatedDTO) throws UsersNotExistException, UsersAlreadyExistException {
        User user = userSession.getUserInSession();

        Optional.ofNullable(userUpdatedDTO.getDisplayName()).ifPresent(user::setDisplayName);
        Optional.ofNullable(userUpdatedDTO.getBannerPicture()).ifPresent(user::setBannerPicture);
        Optional.ofNullable(userUpdatedDTO.getProfilePicture()).ifPresent(user::setProfilePicture);
        Optional.ofNullable(userUpdatedDTO.getDescription()).ifPresent(user::setDescription);
        Optional.ofNullable(userUpdatedDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userUpdatedDTO.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(userUpdatedDTO.getPronouns()).ifPresent(user::setPronouns);

        return projectionFactory.createProjection(UserProjection.class, userService.updateUser(user));
    }

    @PostMapping("{username}/message")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
    })
    public UserToUserMessageProjection sendMessage(@AuthenticationPrincipal UserSession userSession, @PathVariable String username, @RequestBody MessageDTO messageDTO) throws UsersNotExistException {
        User receiver = userService.getUser(username);

        isUserSessionNotReceiverOrThrowException(userSession, receiver);
 
        UserSendMessageToUser message = messageService.sendMessageToUser(messageDTO.getContent(), userSession.getUserInSession(), receiver);

        return new UserToUserMessageProjection(
            message.getMessage().getMessageUUID(), 
            message.getMessage().getContent(), 
            projectionFactory.createProjection(OtherUserProjection.class, message.getSender()), 
            projectionFactory.createProjection(OtherUserProjection.class, message.getReceiver()),
            message.getMessage().getSendedAt()
        );
    }

    @GetMapping("{username}/messages")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
    })
    public List<UserToUserMessageProjection> getMessages(@AuthenticationPrincipal UserSession userSession, @PathVariable String username, @RequestParam(required = false) Integer page) throws UsersNotExistException {
        User receiver = userService.getUser(username);

        isUserSessionNotReceiverOrThrowException(userSession, receiver);

        if(page == null) page = 0;

        return messageService.getMessagesFromSenderAndReceiver(userSession.getUserInSession(), receiver, page)
            .stream()
            .map(message -> new UserToUserMessageProjection(
                message.getMessage().getMessageUUID(), 
                message.getMessage().getContent(), 
                projectionFactory.createProjection(OtherUserProjection.class, message.getSender()), 
                projectionFactory.createProjection(OtherUserProjection.class, message.getReceiver()),
                message.getMessage().getSendedAt()
            ))
            .toList();
    }

    private void isUserSessionNotReceiverOrThrowException(UserSession userSession, User user) throws UsersNotExistException {
        if (userSession.getUserInSession().equals(user)) throw new UsersNotExistException();
    }
}
