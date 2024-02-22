package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.exception.UsersException.UsersNotExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.UserService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired 
    private ProjectionFactory projectionFactory;
    
    @SuppressWarnings("null")
    @GetMapping
    public UserProjection getUser(Principal principal) throws UsersNotExistException {
        User user = userService.getUser(principal.getName());
        return projectionFactory.createProjection(UserProjection.class, user);
    }
}
