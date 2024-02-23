package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.UserSession;
import ch.shkermit.tpi.chatapp.projection.UserProjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired 
    private ProjectionFactory projectionFactory;
    
    @SuppressWarnings("null")
    @GetMapping
    public UserProjection getUser(@AuthenticationPrincipal UserSession userSession) throws UsersNotExistException {
        return projectionFactory.createProjection(UserProjection.class, userSession.getUserInSession());
    }
}
