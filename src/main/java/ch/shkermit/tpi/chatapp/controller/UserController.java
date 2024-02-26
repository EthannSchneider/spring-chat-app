package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.dto.UpdateUserDTO;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.OtherUserProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SuppressWarnings("null")
@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired 
    private ProjectionFactory projectionFactory;

    @Autowired
    private UserService userService;
    
    @GetMapping
    public UserProjection getUser(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return projectionFactory.createProjection(UserProjection.class, userSession.getUserInSession());
    }

    @GetMapping("{username}")
    public OtherUserProjection getUserByUsername(@PathVariable String username) throws UsersNotExistException {
        return projectionFactory.createProjection(OtherUserProjection.class, userService.getUser(username));
    }

    @PutMapping
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
}
