package ch.shkermit.tpi.chatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.shkermit.tpi.chatapp.dto.RegisterUserDTO;
import ch.shkermit.tpi.chatapp.exception.UsersException.UsersAlreadyExistException;
import ch.shkermit.tpi.chatapp.model.User;
import ch.shkermit.tpi.chatapp.projection.TokenUserProjection;
import ch.shkermit.tpi.chatapp.projection.UserProjection;
import ch.shkermit.tpi.chatapp.service.UserService;
import ch.shkermit.tpi.chatapp.service.UserSessionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth")
public class Authentification {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Autowired
    private ProjectionFactory projectionFactory;

    @PostMapping("login")
    public String login() {
        return "Login";
    }

    @PostMapping("register")
    public ResponseEntity<TokenUserProjection> register(@RequestBody RegisterUserDTO user) throws UsersAlreadyExistException {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(user.getPassword());

        userService.createUser(newUser);

        String token = userSessionService.createSession(newUser);

        UserProjection tokenUser = projectionFactory.createProjection(UserProjection.class, newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenUserProjection(token, tokenUser));
    }

    @GetMapping("logout")
    public String logout() {
        return "Logout";
    }
}
